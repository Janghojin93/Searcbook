package com.kakaopay.searchbook.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kakaopay.searchbook.data.db.book.BookDao
import com.kakaopay.searchbook.data.model.book.Book
import com.kakaopay.searchbook.utils.Converters


@Database(entities = [Book::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
