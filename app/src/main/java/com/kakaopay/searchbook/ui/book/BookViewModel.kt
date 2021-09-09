package com.kakaopay.searchbook.ui.book

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaopay.searchbook.app.PAGE_SIZE
import com.kakaopay.searchbook.app.SEARCH_BOOK_TAG
import com.kakaopay.searchbook.data.model.book.Book
import com.kakaopay.searchbook.data.model.responce.SearchBookResponce
import com.kakaopay.searchbook.data.network.Resource
import com.kakaopay.searchbook.data.repository.book.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import com.kakaopay.searchbook.ui.book.SearchBookEvent.*


@HiltViewModel
class BookViewModel @Inject constructor(private val bookRepository: BookRepository) :
    ViewModel() {

    val searchBook: MutableLiveData<Resource<SearchBookResponce>> = MutableLiveData()
    var searchBookPage = 1
    private var searchBookResponse: SearchBookResponce? = null
    private var oldSearchQuery = ""
    private var oldsearchBookPage = 1

    init {
        newSearchBook("카카오")
    }


    fun newSearchBook(searchQuery: String) {
        searchBookPage = 1
        searchBookResponse = null

        //방금전에 같은쿼리와 같은 페이지번호로 요청을 했다면 실행되지않도록 한다.(중복방지)
        if (!oldSearchQuery.equals(searchQuery) || oldsearchBookPage != searchBookPage) {
            searchBook(searchQuery)
            oldSearchQuery = searchQuery
            oldsearchBookPage = searchBookPage
        }
    }

    fun pagingSearchBook(searchQuery: String) {
        searchBook(searchQuery)
    }


    private fun searchBook(searchQuery: String) =
        viewModelScope.launch {
            searchBook.postValue(Resource.Loading())
            Log.d(SEARCH_BOOK_TAG, "searchQuery::${searchQuery}    searchBookPage::${searchBookPage}")
            val response = bookRepository.getSearchBook(searchQuery, searchBookPage, PAGE_SIZE)
            searchBook.postValue(handleBookResponse(response))
        }


    private fun handleBookResponse(response: Response<SearchBookResponce>): Resource<SearchBookResponce> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchBookPage++
                if (searchBookResponse == null) {
                    searchBookResponse = resultResponse
                } else {
                    val oldBookList = searchBookResponse?.documents
                    val newBookList = resultResponse.documents
                    oldBookList?.addAll(newBookList)
                    searchBookResponse?.meta = resultResponse.meta
                }
                return Resource.Success(searchBookResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}
