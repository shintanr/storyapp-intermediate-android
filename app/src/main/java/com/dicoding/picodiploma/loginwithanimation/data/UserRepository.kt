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
import android.util.Log

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,

) {

    fun getStory(token: String): LiveData<ResultState<List<ListStoryItem>>> = liveData{
        emit(ResultState.Loading)
        try{
            val response = apiService.getStories("Bearer $token")
            emit(ResultState.Success(response.listStory))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }catch (e: Exception){
            emit(ResultState.Error(e.message ?: "Error"))
        }
    }

    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.login(email, password)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            errorBody?.let {
                val errorResponse = Gson().fromJson(it, ErrorResponse::class.java)
                emit(ResultState.Error(errorResponse.message))
            } ?: emit(ResultState.Error("Unknown HTTP error"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun signup(name: String, email: String, password: String): LiveData<ResultState<RegisterResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            errorBody?.let {
                val errorResponse = Gson().fromJson(it, ErrorResponse::class.java)
                emit(ResultState.Error(errorResponse.message))
            } ?: emit(ResultState.Error("Unknown HTTP error"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An unknown error occurred"))
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
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepository(apiService, userPreference)
            }.also { INSTANCE = it }
    }
}
