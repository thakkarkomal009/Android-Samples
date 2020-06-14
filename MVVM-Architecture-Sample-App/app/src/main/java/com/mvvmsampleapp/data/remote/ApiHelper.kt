package com.mvvmsampleapp.data.remote

import com.mvvmsampleapp.data.model.LoginModel
import com.mvvmsampleapp.data.model.user.UserListModel
import retrofit2.Call
import retrofit2.http.*

interface ApiHelper {
    @FormUrlEncoded
    @POST("/api/login")
    fun doLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginModel>

    @GET("/api/users")
    @Headers("Content-Type: application/json")
    fun getUserList(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Call<UserListModel>
}