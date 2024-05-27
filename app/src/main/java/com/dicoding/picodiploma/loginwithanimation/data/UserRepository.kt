package com.dicoding.picodiploma.loginwithanimation.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.picodiploma.loginwithanimation.data.api.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import retrofit2.Response


class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    fun getStory(): LiveData<ResultState<List<ListStoryItem>>> = liveData{
        emit(ResultState.Loading)
        try{
            val response = apiService.getStories("")
            emit(ResultState.Success(response.listStory))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }catch (e: Exception){
            emit(ResultState.Error(e.message ?: "Error"))
        }
    }

    fun login(email: String, password: String) : LiveData<ResultState<LoginResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.login(email, password)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val body = Gson().fromJson(error, ErrorResponse::class.java)
            emit(ResultState.Error(body.message))
        }
    }

    fun signup(name: String, email: String, password: String): LiveData<ResultState<RegisterResponse>> = liveData{
        emit(ResultState.Loading)
        try {
            val response = apiService.register(name,email,password)
            emit(ResultState.Success(response))
        }catch (e: HttpException){
            val error = e.response()?.errorBody()?.string()
            val body = Gson().fromJson(error, ErrorResponse::class.java)
            emit(ResultState.Error(body.message))
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun clearInstance() {
            INSTANCE = null
        }
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepository(userPreference, apiService)
            }.also { INSTANCE = it }
    }
}