package com.example.data.module

import com.example.data.api.KakaoApiService
import com.example.data.db.ImageDao
import com.example.data.repository.ImageRepository
import com.example.data.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideSearchRepository(
        kakaoApiService: KakaoApiService
    ): SearchRepository = SearchRepository(kakaoApiService)

    @Singleton
    @Provides
    fun provideImageRepository(
        imageDao: ImageDao
    ): ImageRepository = ImageRepository(imageDao)
}