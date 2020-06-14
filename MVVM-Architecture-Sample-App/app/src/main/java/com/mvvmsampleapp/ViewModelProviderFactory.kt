package com.mvvmsampleapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvvmsampleapp.ui.login.LoginViewModel
import com.mvvmsampleapp.ui.user.UserListItemModel
import com.mvvmsampleapp.ui.user.UserListViewModel
import com.mvvmsampleapp.ui.user_detail.UserDetailViewModel
import com.mvvmsampleapp.util.rx.SchedulerProvider
import javax.inject.Inject
import javax.inject.Singleton

//Inject all view models
@Suppress("UNCHECKED_CAST")
@Singleton
class ViewModelProviderFactory @Inject
constructor(
    private val schedulerProvider: SchedulerProvider, private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                schedulerProvider, context
            ) as T
            modelClass.isAssignableFrom(UserListViewModel::class.java) -> UserListViewModel(
                schedulerProvider, context
            ) as T
            modelClass.isAssignableFrom(UserDetailViewModel::class.java) -> UserDetailViewModel(
                schedulerProvider, context
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}