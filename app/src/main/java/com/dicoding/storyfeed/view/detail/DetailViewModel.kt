package com.dicoding.storyfeed.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyfeed.repo.StoryRepository
import com.dicoding.storyfeed.response.Story
import kotlinx.coroutines.launch

class DetailViewModel(private val storyRepository: StoryRepository) : ViewModel(){
    private val _stories = MutableLiveData<Story>()
    val detailStories: LiveData<Story> get() = _stories

    fun fetchStories(id: String) {
        viewModelScope.launch {
            try {
                val response = storyRepository.detailStories(id)
                _stories.value = response.story
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}