package com.dicoding.storyfeed.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyfeed.di.ResultState
import com.dicoding.storyfeed.repo.StoryRepository
import com.dicoding.storyfeed.response.ListStoryItem
import kotlinx.coroutines.launch

class MapViewModel(private val storyRepository: StoryRepository): ViewModel() {
    private val _stories = MutableLiveData<ResultState<List<ListStoryItem>>>()
    val stories: LiveData<ResultState<List<ListStoryItem>>> get() = _stories

    fun fetchStories() {
        viewModelScope.launch {
            _stories.value = ResultState.Loading
            try {
                val response = storyRepository.getStoriesWithLocation()
                _stories.value = ResultState.Success(response.listStory)
            } catch (e: Exception) {
                _stories.value = ResultState.Error(e.message ?: "An error occurred")
            }
        }
    }
}
