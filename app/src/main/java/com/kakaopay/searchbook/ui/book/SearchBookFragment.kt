package com.kakaopay.searchbook.ui.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.kakaopay.searchbook.R
import com.kakaopay.searchbook.app.App
import com.kakaopay.searchbook.data.model.book.Book
import com.kakaopay.searchbook.data.network.Resource
import com.kakaopay.searchbook.databinding.FragmentSearchBookBinding
import com.kakaopay.searchbook.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchBookFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchBookBinding

    override val layoutId = R.layout.fragment_search_book

    private lateinit var mRootView: View

    private val bookViewModel: BookViewModel by activityViewModels()

    private var listOfBook = ArrayList<Book>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getSearchBook("친구")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = binding.root

       /* binding.fasdasd.onThrottleClick {
            Log.d("asdafasdasdasdasd1asd23", "버튼 2 클릭");
            (activity as BookActivity).openBookDetailFragment()
        }*/

        return mRootView


    }


    companion object {
        @JvmStatic
        fun newInstance() =
            SearchBookFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


    private fun getSearchBook(query: String) {
        bookViewModel.searchBook(query)
        if (getNetworkConnected(App.getMyApplicationContext())) {
            bookViewModel.searchBookList.observe(this, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { bookResponse ->
                            Log.d("httptetest", "성공::" + bookResponse.documents[0].isbn);
                        }
                    }

                    is Resource.Error -> {
                        response.message?.let { message ->
                            Log.d("httptetest", "에러::" + message);
                        }
                    }

                    is Resource.Loading -> {
                        Log.d("httptetest", "로딩");
                    }
                }
            })

        } else {
            Log.d("httptetest", "인터넷연결을 확인하세요");
        }
    }
}