package com.dicoding.storyfeed.view.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyfeed.api.UploadStoryResponse
import com.dicoding.storyfeed.repo.StoryRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class AddViewModel (private val storyRepository: StoryRepository): ViewModel() {
    private val _storiesResponse = MutableLiveData<UploadStoryResponse>()
    val storiesResponse: LiveData<UploadStoryResponse> get() = _storiesResponse

    fun uploadStory(imageFile: File) {
        Log.d("Image File", "showImage: ${imageFile.path}")
        val description = "Ini adalah deskripsi gambar"

        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        viewModelScope.launch {
            try {
                val successResponse = storyRepository.addStories(file = multipartBody, description = requestBody)
                _storiesResponse.postValue(successResponse)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, UploadStoryResponse::class.java)
                _storiesResponse.postValue(errorResponse)
            }
        }
    }
}