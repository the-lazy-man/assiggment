package com.example.assiggment.di

import com.example.assiggment.data.local.TokenManager
import com.example.assiggment.data.remote.PeanutApi
import com.example.assiggment.data.remote.SoapManager
import com.example.assiggment.data.repository.AuthRepositoryImpl
import com.example.assiggment.data.repository.MainRepositoryImpl
import com.example.assiggment.domain.repository.AuthRepository
import com.example.assiggment.domain.repository.MainRepository
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
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val gson = com.google.gson.GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl("https://peanut.ifxdb.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun providePeanutApi(retrofit: Retrofit): PeanutApi {
        return retrofit.create(PeanutApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: PeanutApi, tokenManager: TokenManager): AuthRepository {
        return AuthRepositoryImpl(api, tokenManager)
    }

    @Provides
    @Singleton
    fun provideSoapManager(client: OkHttpClient): SoapManager {
        return SoapManager(client)
    }

    @Provides
    @Singleton
    fun provideMainRepository(api: PeanutApi, soapManager: SoapManager): MainRepository {
        return MainRepositoryImpl(api, soapManager)
    }
}
