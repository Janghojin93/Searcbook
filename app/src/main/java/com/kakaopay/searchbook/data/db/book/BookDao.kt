package com.kakaopay.searchbook.data.db.book

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kakaopay.searchbook.data.model.book.Book

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(book: Book)

    @Query("SELECT * FROM book_table")
    fun getBooks(): LiveData<List<Book>>

    @Query("DELETE FROM book_table WHERE isbn = :isbn")
    fun deleteBook(isbn: String)

}
