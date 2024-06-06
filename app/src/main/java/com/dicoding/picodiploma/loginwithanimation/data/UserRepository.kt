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
import com.dicoding.picodiploma.loginwithanimation.response.UploadStoryResponse
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    fun uploadStory(image: File, description: String): LiveData<ResultState<UploadStoryResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val token = userPreference.getToken().first()
            val requestDescription = description.toRequestBody("text/plain".toMediaType())
            val requestImage = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart = MultipartBody.Part.createFormData(
                "photo",
                image.name,
                requestImage
            )
            val response = apiService.uploadStory("Bearer $token", imageMultipart, requestDescription)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }


    fun getStory(): LiveData<ResultState<List<ListStoryItem>>> = liveData{
        emit(ResultState.Loading)
        try{
            val response = apiService.getStories()
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
                INSTANCE ?: UserRepository(userPreference, apiService)
            }.also { INSTANCE = it }
    }
}
