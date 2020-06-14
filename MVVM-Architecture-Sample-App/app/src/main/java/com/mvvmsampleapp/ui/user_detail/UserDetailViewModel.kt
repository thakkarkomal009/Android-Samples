package com.mvvmsampleapp.ui.user_detail

import android.content.Context
import com.mvvmsampleapp.ui.base.BaseViewModel
import com.mvvmsampleapp.util.rx.SchedulerProvider

class UserDetailViewModel(
    schedulerProvider: SchedulerProvider, context: Context
) :
    BaseViewModel<UserDetailNavigator>(schedulerProvider, context) {
}