package com.mvvmsampleapp.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginModel {
    @SerializedName("token")
    @Expose
    private var token: String? = null

    fun getToken(): String? {
        return token
    }

    fun setToken(token: String?) {
        this.token = token
    }
}