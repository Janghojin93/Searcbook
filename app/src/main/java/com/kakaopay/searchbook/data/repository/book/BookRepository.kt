package com.kakaopay.searchbook.data.repository.book

import android.content.Context
import com.kakaopay.searchbook.data.network.ApiServices
import com.kakaopay.searchbook.data.db.book.BookDao
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val apiServices: ApiServices,
    private val bookDao: BookDao,
    @ApplicationContext val context: Context
) {

    suspend fun getSearchBook(query: String, page: Int, size: Int) = apiServices.getSearchBook(query, page, size)

}
