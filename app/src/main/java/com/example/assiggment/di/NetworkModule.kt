package com.example.assiggment.di

import com.example.assiggment.data.remote.PeanutApi
import com.example.assiggment.data.repository.AuthRepositoryImpl
import com.example.assiggment.domain.repository.AuthRepository
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
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://peanut.ifxdb.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePeanutApi(retrofit: Retrofit): PeanutApi {
        return retrofit.create(PeanutApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: PeanutApi): AuthRepository {
        return AuthRepositoryImpl(api)
    }
}
