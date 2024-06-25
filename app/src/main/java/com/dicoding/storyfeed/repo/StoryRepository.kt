package com.dicoding.storyfeed.repo

import com.dicoding.storyfeed.api.ApiService
import com.dicoding.storyfeed.response.DetailResponse
import com.dicoding.storyfeed.response.StoryResponse
import com.dicoding.storyfeed.response.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository (private val apiService: ApiService){
    suspend fun getStories(): StoryResponse {
        return apiService.getStories()
    }

    suspend fun detailStories(id: String): DetailResponse {
        return apiService.detailStories(id)
    }

    suspend fun addStories(description: RequestBody, file: MultipartBody.Part): UploadStoryResponse {
        return apiService.uploadStory(file, description)
    }
}