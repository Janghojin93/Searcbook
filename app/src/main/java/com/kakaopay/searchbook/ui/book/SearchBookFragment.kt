package com.kakaopay.searchbook.ui.book

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakaopay.searchbook.R
import com.kakaopay.searchbook.app.App
import com.kakaopay.searchbook.app.SEARCH_BOOK_TIME_DELAY
import com.kakaopay.searchbook.data.network.Resource
import com.kakaopay.searchbook.databinding.FragmentSearchBookBinding
import com.kakaopay.searchbook.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class SearchBookFragment : BaseFragment() {

    @Inject
    lateinit var application: App
    private lateinit var binding: FragmentSearchBookBinding
    private lateinit var mRootView: View
    private val bookViewModel: BookViewModel by activityViewModels()

    override val layoutId = R.layout.fragment_search_book


    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    private val TAG = "SearchBookFragment"

    private val searchbookAdapter: SearchBookAdapter by lazy {
        SearchBookAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = binding.root



        return mRootView


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        initObsever()
        initUi()

        binding.edittextSearchbookSearchbar.apply {
            afterTextChangedCustom(SEARCH_BOOK_TIME_DELAY) {
                if (getNetworkConnected(application)) {
                    if (it != "") {
                        bookViewModel.newSearchBook(it)
                    }
                } else {
                    Toast.makeText(activity, R.string.no_internet_connection, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchBookFragment()
    }


    private fun initObsever() {
        bookViewModel.searchBook.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    var isList = true
                    hideProgressBar()
                    response.data?.let { bookResponse ->
                        Log.d(TAG, "성공:: ${bookResponse.meta.is_end}");
                        searchbookAdapter.submitList(bookResponse.documents.toList())
                        isLastPage = bookResponse.meta.is_end
                        isList = bookResponse.documents.toList().size != 0
                    }

                    //서버로부터 불러온 책 리스트가 존재하지않을때 리사이클러뷰는 가려지고 EmptyView가 보여진다.
                    if (!isList) {
                        showEmptyView()
                    } else {
                        hideEmptyView()
                    }

                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.d(TAG, "에러:: ${message}");
                        Toast.makeText(
                            activity,
                            R.string.no_internet_connection,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                    Log.d(TAG, "로딩중");
                }
            }
        })
    }


    private fun setupRecyclerView() {
        binding.recyclerviewSearchbookBooklist.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = searchbookAdapter
            addOnScrollListener(this@SearchBookFragment.scrollListener)
        }


        searchbookAdapter.onBookClicked = { postion ->
            Log.d(TAG, "아이템클릭::${postion}");
            bookViewModel.updateDetailBook(postion)
            (activity as BookActivity).openBookDetailFragment()
        }


    }

    private fun initUi() {
        binding.floatingbuttonSearchbookMoveTop.hide()
        binding.floatingbuttonSearchbookMoveTop.onThrottleClick {
            binding.recyclerviewSearchbookBooklist.smoothScrollToPosition(0)
        }
    }


    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

            Log.d(
                TAG,
                "[firstVisibleItemPosition::${firstVisibleItemPosition}] :: [lastVisibleItemPosition::${lastVisibleItemPosition}]"
            )

            //사용자가리사이클러뷰의 포지션을 20이상 스크롤했을때 플로팅버튼이 보여진다.
            if (lastVisibleItemPosition > 20) {
                showFloatingButton()
            } else {
                hideFloatingButton()
            }

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            Log.d(
                TAG,
                "[visibleItemCount::${visibleItemCount}] :: [totalItemCount::${totalItemCount}]"
            )

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 50
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                if (getNetworkConnected(application)) {
                    bookViewModel.pagingSearchBook(binding.edittextSearchbookSearchbar.text.toString())
                    isScrolling = false
                    Log.d(TAG, "scrollListener::");
                } else {
                    Toast.makeText(activity, R.string.no_internet_connection, Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                binding.recyclerviewSearchbookBooklist.setPadding(0, 0, 0, 0)
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun hideProgressBar() {
        binding.ProgressBarSearchbookLoadingbar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.ProgressBarSearchbookLoadingbar.visibility = View.VISIBLE
        isLoading = true
    }


    private fun hideEmptyView() {
        binding.recyclerviewSearchbookBooklist.visibility = View.VISIBLE
        binding.imageviewEmptyviewEmptyImage.visibility = View.GONE
        binding.imageviewEmptyviewEmptyTextTop.visibility = View.GONE
        binding.imageviewEmptyviewEmptyTextBottom.visibility = View.GONE

    }

    private fun showEmptyView() {
        binding.recyclerviewSearchbookBooklist.visibility = View.GONE
        binding.imageviewEmptyviewEmptyImage.visibility = View.VISIBLE
        binding.imageviewEmptyviewEmptyTextTop.visibility = View.VISIBLE
        binding.imageviewEmptyviewEmptyTextBottom.visibility = View.VISIBLE
        hideFloatingButton()
    }

    private fun showFloatingButton() {
        binding.floatingbuttonSearchbookMoveTop.show()
    }

    private fun hideFloatingButton() {
        binding.floatingbuttonSearchbookMoveTop.hide()
    }

}