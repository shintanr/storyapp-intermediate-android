package com.dicoding.picodiploma.loginwithanimation.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.response.UploadStoryResponse
import java.io.File

class UploadViewModel(private val repository: UserRepository) : ViewModel() {

    fun uploadImage(image: File, description: String): LiveData<ResultState<UploadStoryResponse>> {
        return repository.uploadStory(image, description)
    }
}