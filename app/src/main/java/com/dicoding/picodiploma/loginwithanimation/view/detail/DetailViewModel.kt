package com.dicoding.picodiploma.loginwithanimation.view.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository

class DetailViewModel(private val repository: UserRepository): ViewModel() {

    private val storyId = MutableLiveData<String>()

    fun setStoryId(id: String) {
        storyId.value = id
    }
    val detailStory = storyId.switchMap {
        repository.getDetailStory(it)
    }
}