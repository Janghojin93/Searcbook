package com.kakaopay.searchbook.di


import android.content.Context
import androidx.room.Room
import com.kakaopay.searchbook.BuildConfig
import com.kakaopay.searchbook.app.App
import com.kakaopay.searchbook.app.DB_NAME
import com.kakaopay.searchbook.data.network.ApiServices
import com.kakaopay.searchbook.data.db.AppDatabase
import com.kakaopay.searchbook.data.db.book.BookDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): App {
        return app as App
    }

    @Singleton
    @Provides
    fun provideBookService(): ApiServices {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .build()


    @Singleton
    @Provides
    fun provideBookDao(db: AppDatabase): BookDao = db.bookDao()

}
