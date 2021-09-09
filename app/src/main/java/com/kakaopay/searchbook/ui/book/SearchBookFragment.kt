package com.kakaopay.searchbook.ui.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakaopay.searchbook.R
import com.kakaopay.searchbook.app.App
import com.kakaopay.searchbook.app.PAGE_SIZE
import com.kakaopay.searchbook.app.SEARCH_BOOK_TAG
import com.kakaopay.searchbook.app.SEARCH_BOOK_TIME_DELAY
import com.kakaopay.searchbook.data.network.Resource
import com.kakaopay.searchbook.databinding.FragmentSearchBookBinding
import com.kakaopay.searchbook.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject


@AndroidEntryPoint
class SearchBookFragment : BaseFragment() {

    @Inject
    lateinit var application: App
    private lateinit var binding: FragmentSearchBookBinding
    private lateinit var mRootView: View
    private val bookViewModel: BookViewModel by activityViewModels()

    override val layoutId = R.layout.fragment_search_book


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

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
        setupRecyclerView()


        return mRootView


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.edittextSearchbookSearchbar.apply {
            afterTextChangedCustom(SEARCH_BOOK_TIME_DELAY) {
                if (getNetworkConnected(application)) {
                    Log.d(SEARCH_BOOK_TAG, "검색::" + it);
                    bookViewModel.newSearchBook(it)
                } else {
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
        }

        initObsever()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchBookFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


    private fun initObsever() {
        bookViewModel.searchBook.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    var isList = true
                    hideProgressBar()
                    response.data?.let { bookResponse ->
                        Log.d(SEARCH_BOOK_TAG, "성공:: ${bookResponse.meta.is_end}");
                        searchbookAdapter.submitList(bookResponse.documents.toList())
                        isLastPage = bookResponse.meta.is_end
                        isList = bookResponse.documents.toList().size != 0
                    }

                    if (!isList) {
                        showEmptyView()

                    } else {
                        hideEmptyView()
                    }

                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.d(SEARCH_BOOK_TAG, "에러:: ${message}");
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                    Log.d(SEARCH_BOOK_TAG, "로딩중");
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
    }


    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

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
                    Log.d(SEARCH_BOOK_TAG, "scrollListener::");
                } else {
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT)
                        .show();
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
    }


}