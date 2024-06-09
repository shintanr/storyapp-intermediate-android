package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.data.result.ResultState
import com.dicoding.picodiploma.loginwithanimation.response.LogInResponse


class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<ResultState<LogInResponse>> {
        return authRepository.login(email, password)
    }
}