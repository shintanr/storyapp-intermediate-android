package com.dicoding.picodiploma.loginwithanimation.view.map

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository


class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStoryWithLocation() = storyRepository.getStoryWithLocation()
}