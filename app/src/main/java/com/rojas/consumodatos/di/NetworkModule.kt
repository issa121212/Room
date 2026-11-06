package com.rojas.consumodatos.di

import com.rojas.consumodatos.data.remote.ContactApiService
import com.rojas.consumodatos.data.remote.ContactRemoteDataSource
import com.rojas.consumodatos.data.remote.ContactRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideContactApiService(okHttpClient: OkHttpClient): ContactApiService {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ContactApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideContactRemoteDataSource(impl: ContactRemoteDataSourceImpl): ContactRemoteDataSource = impl
}
