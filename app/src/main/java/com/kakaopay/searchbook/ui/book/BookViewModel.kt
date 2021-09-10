package com.kakaopay.searchbook.ui.book

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaopay.searchbook.app.PAGE_SIZE
import com.kakaopay.searchbook.data.model.book.Book
import com.kakaopay.searchbook.data.model.responce.SearchBookResponce
import com.kakaopay.searchbook.data.network.Resource
import com.kakaopay.searchbook.data.repository.book.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class BookViewModel @Inject constructor(private val bookRepository: BookRepository) :
    ViewModel() {

    private val TAG = "BookViewModel"

    val searchBook: MutableLiveData<Resource<SearchBookResponce>> = MutableLiveData()
    var searchBookPage = 1
    private var searchBookResponse: SearchBookResponce? = null
    private var oldSearchQuery = ""
    private var oldsearchBookPage = 1
    var detailBook: Book? = null
    private var selectDetailBookPosition = 0


    //사용자가 새로운 쿼리로 검색을 했을때 사용하는 함수
    fun newSearchBook(searchQuery: String) {
        Log.d(TAG, "[searchQuery::${searchQuery}]  [searchBookPage::${searchBookPage}]")
        Log.d(TAG, "[oldSearchQuery::${oldSearchQuery}]  [oldsearchBookPage::${oldsearchBookPage}]")

        //방금전에 같은쿼리와 같은 페이지번호로 요청을 했다면 실행되지않도록 한다.(중복방지)
        if (!oldSearchQuery.equals(searchQuery) || oldsearchBookPage + 1 != searchBookPage) {
            Log.d("adsafasdasdf", "[newSearchBook:] ")

            searchBookPage = 1
            searchBookResponse = null

            oldSearchQuery = searchQuery
            oldsearchBookPage = searchBookPage
            searchBook(searchQuery)
        }
    }

    //사용자가 스크롤을 최하단으로 내렸을때 추가로 데이터를 요청하기위한 함수(다음 페이지가 없다면 실행되지않는다.)
    fun pagingSearchBook(searchQuery: String) {

        oldSearchQuery = searchQuery
        oldsearchBookPage = searchBookPage

        searchBook(searchQuery)
    }

    //사용자가 책 검색 리스트에서 아이템을 클릭을하면 BookDetailFragment로 이동을 한다.
    //이때 BookDetailFragment는 detailBook이라는 값을 참조하여 책의 제목이나 콘텐츠 등등을 사용자에게 보여준다.
    //사용자가 BookDetailFragment에서 좋아요버튼을 클릭했을때 SearchBookFragment의 리사이클러뷰에서 적용을 시켜주기위해 selectDetailBookPosition을 저장한다.
    fun updateDetailBook(position: Int) {
        detailBook = searchBookResponse?.documents?.get(position)
        selectDetailBookPosition = position
    }

    //사용자가 BookDetailFragment화면에서 좋아요를 클릭했을때 호출되는 함수이다.
    fun likeBook() {
        if (searchBookResponse?.documents?.get(selectDetailBookPosition)!!.islike) {
            searchBookResponse?.documents?.get(selectDetailBookPosition)!!.islike = false
            detailBook!!.islike = false
        } else {
            searchBookResponse?.documents?.get(selectDetailBookPosition)!!.islike = true
            detailBook!!.islike = true
        }
    }

    private fun searchBook(searchQuery: String) =
        viewModelScope.launch {
            searchBook.postValue(Resource.Loading())

            Log.d(TAG, "[searchQuery::${searchQuery}]  [searchBookPage::${searchBookPage}]")
            Log.d(
                TAG,
                "[oldSearchQuery::${oldSearchQuery}]  [oldsearchBookPage::${oldsearchBookPage}]"
            )

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
