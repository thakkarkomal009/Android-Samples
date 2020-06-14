package com.mvvmsampleapp.ui.login

import com.mvvmsampleapp.ui.base.BaseNavigator

interface LoginNavigator : BaseNavigator {
    fun login()
    fun signInSuccess(message: String)
}