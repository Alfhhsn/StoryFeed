package com.dicoding.storyfeed.view.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyfeed.repo.StoryRepository
import com.dicoding.storyfeed.response.ListStoryItem

class MainViewModel (private val storyRepository: StoryRepository) : ViewModel(){
    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories = storyRepository.getStories()

}