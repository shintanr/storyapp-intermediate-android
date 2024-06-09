package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.data.result.ResultState
import com.dicoding.picodiploma.loginwithanimation.response.RegisterResponse

class SignupViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun register(name: String, email: String, password: String): LiveData<ResultState<RegisterResponse>> {
        return authRepository.register(name, email, password)
    }
}