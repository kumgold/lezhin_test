package com.example.data.module

import android.content.Context
import com.example.data.db.ImageDao
import com.example.data.db.LezhinDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideLezhinDatabase(
        @ApplicationContext context: Context
    ): LezhinDatabase = LezhinDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideImageDao(
        database: LezhinDatabase
    ): ImageDao = database.imageDao()
}