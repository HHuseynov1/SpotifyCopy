package com.example.spotifycopy.data.other.di

import com.example.spotifycopy.SwipeSongAdapter
import com.example.spotifycopy.data.repo.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRepository() : Repository{
        return Repository()
    }
    @Provides
    fun provideSwipeSongAdapter() = SwipeSongAdapter()
}