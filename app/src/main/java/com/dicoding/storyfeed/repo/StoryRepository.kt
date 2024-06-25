package com.dicoding.storyfeed.repo

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyfeed.api.ApiService
import com.dicoding.storyfeed.data.PagingSource
import com.dicoding.storyfeed.response.DetailResponse
import com.dicoding.storyfeed.response.ListStoryItem
import com.dicoding.storyfeed.response.StoryResponse
import com.dicoding.storyfeed.response.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository (private val apiService: ApiService){
    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PagingSource(apiService) }
        ).liveData
    }

    suspend fun detailStories(id: String): DetailResponse {
        return apiService.detailStories(id)
    }

    suspend fun addStories(description: RequestBody, file: MultipartBody.Part): UploadStoryResponse {
        return apiService.uploadStory(file, description)
    }

    suspend fun getStoriesWithLocation(): StoryResponse {
        return apiService.getStoriesWithLocation()
    }
}