package com.dicoding.picodiploma.loginwithanimation.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.result.ResultState
import com.dicoding.picodiploma.loginwithanimation.response.UploadResponse
import java.io.File


class UploadViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun uploadImage(image: File, description: String): LiveData<ResultState<UploadResponse>> {
        return storyRepository.uploadStory(image, description)
    }
}