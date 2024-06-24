package com.dicoding.storyfeed.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyfeed.repo.StoryRepository
import com.dicoding.storyfeed.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel (private val storyRepository: StoryRepository) : ViewModel(){
    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> get() = _stories

    fun fetchStories() {
        viewModelScope.launch {
            try {
                val response = storyRepository.getStories()
                _stories.value = response.listStory
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}