package com.mvvmsampleapp.data.repository

import com.mvvmsampleapp.data.model.LoginModel
import com.mvvmsampleapp.data.model.user.UserListModel
import com.mvvmsampleapp.data.remote.ApiEndPoint
import com.mvvmsampleapp.data.remote.ApiHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MVVMRepository @Inject
constructor() : ApiHelper {

    override fun doLogin(email: String, password: String): Call<LoginModel> {
        return apiService.doLogin(email, password)
    }

    override fun getUserList(page: Int, per_page: Int): Call<UserListModel> {
        return apiService.getUserList(page, per_page)
    }

    companion object {

        private fun getServiceApi(retrofit: Retrofit): ApiHelper =
            retrofit.create(ApiHelper::class.java)

        var logging =
            HttpLoggingInterceptor()
        var okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(ApiEndPoint.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val apiService = getServiceApi(getRetrofit())
    }
}