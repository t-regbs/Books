package com.example.books.app.di

import android.app.Application
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.books.R
import com.example.books.data.api.BookService
import com.example.books.data.db.BooksDao
import com.example.books.data.db.BooksDatabase
import com.example.books.data.repository.BooksRepository
import com.example.books.ui.BookListViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://www.googleapis.com/books/v1/"

val viewModelModule = module {
    viewModel {
        BookListViewModel(get())
    }
}

val apiModule = module {
    fun provideBookService(retrofit: Retrofit): BookService = retrofit.create(BookService::class.java)

    single { provideBookService(get()) }
}

val netModule = module {

    fun provideHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        val okHttPClientBuilder = OkHttpClient.Builder().addInterceptor(logger)
        return okHttPClientBuilder.build()
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
    }

    single { provideHttpClient() }
    single { provideRetrofit(get()) }
}

val databaseModule = module {

    fun provideDatabase(application: Application): BooksDatabase {
        return Room.databaseBuilder(application,
                BooksDatabase::class.java,
                "Books.db")
                .build()
    }
    single { provideDatabase(androidApplication()) }
}

val repositoryModule = module {
    fun provideRepository(api: BookService, database: BooksDatabase): BooksRepository {
        return BooksRepository(api, database)
    }

    single { provideRepository(get(), get()) }
}

val glideModule = module {
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
                .placeholderOf(R.drawable.book_open)
                .error(R.drawable.book_open)
    }

    fun provideGlideInstance(application: Application, requestOptions: RequestOptions): RequestManager {
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions)
    }

    single { provideRequestOptions() }
    single { provideGlideInstance(androidApplication(), get()) }
}