package com.dicoding.storyfeed.di

import android.content.Context
import com.dicoding.storyfeed.api.ApiConfig
import com.dicoding.storyfeed.preferences.UserPreferences
import com.dicoding.storyfeed.repo.StoryRepository
import com.dicoding.storyfeed.repo.UserRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val userPreferences = UserPreferences.getInstance(context)
        val tokenFlow = userPreferences.tokenFlow
        val apiService = ApiConfig.getApiService(tokenFlow)
        return UserRepository.getInstance(apiService, userPreferences)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val userPreferences = UserPreferences.getInstance(context)
        val tokenFlow = userPreferences.tokenFlow
        val apiService = ApiConfig.getApiService(tokenFlow)
        return StoryRepository(apiService)
    }

}