package com.dicoding.picodiploma.loginwithanimation.data.pref

data class UserModel (
    val token: String,
    val email: String,
    val password: String,
    val isLogin: Boolean = false
)
