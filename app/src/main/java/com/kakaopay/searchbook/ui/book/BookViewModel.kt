package com.kakaopay.searchbook.ui.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaopay.searchbook.data.model.responce.SearchBookResponce
import com.kakaopay.searchbook.data.network.Resource
import com.kakaopay.searchbook.data.repository.book.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val bookRepository: BookRepository) :
    ViewModel() {

    val searchBookList: MutableLiveData<Resource<SearchBookResponce>> = MutableLiveData()
    private val pageSize = 50
    private var pageNum = 1

    fun searchBook(query: String) = viewModelScope.launch {
        getSearchBook(query)
    }


    private suspend fun getSearchBook(query: String) {

        searchBookList.postValue(Resource.Loading())

        try {
            val response = bookRepository.getSearchBook(query, pageNum, pageSize)
            searchBookList.postValue(handleBookResponse(response))

        } catch (t: Throwable) {

            when (t) {
                is IOException -> {
                    searchBookList.postValue(
                        Resource.Error(
                            "에러"
                        )
                    )
                }
                else -> {
                    searchBookList.postValue(
                        Resource.Error(
                            "에러"
                        )
                    )
                }
            }
        }
    }


    private fun handleBookResponse(response: Response<SearchBookResponce>): Resource<SearchBookResponce> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}
