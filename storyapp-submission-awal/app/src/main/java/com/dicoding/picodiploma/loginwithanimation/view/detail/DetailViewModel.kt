package com.dicoding.picodiploma.loginwithanimation.view.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository

class DetailViewModel(private val storyRepository: StoryRepository): ViewModel() {

    private val storyId = MutableLiveData<String>()

    fun setStoryId(id: String) {
        storyId.value = id
    }
    val detailStory = storyId.switchMap {
        storyRepository.getDetailStory(it)
    }
}