package com.example.data.module

import com.example.data.BuildConfig
import com.example.data.api.KakaoApiService
import com.example.data.interceptor.KakaoApiInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideKakaoApiService(): KakaoApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_BASE_URL)
            .client(OkHttpClient.Builder().addInterceptor(KakaoApiInterceptor()).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoApiService::class.java)
    }
}