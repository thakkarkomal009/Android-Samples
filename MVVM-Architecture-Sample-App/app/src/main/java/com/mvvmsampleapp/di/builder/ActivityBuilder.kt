package com.mvvmsampleapp.di.builder

import com.mvvmsampleapp.ui.login.LoginActivity
import com.mvvmsampleapp.ui.user.UserListActivity
import com.mvvmsampleapp.ui.user_detail.UserDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun bindUserListActivity(): UserListActivity

    @ContributesAndroidInjector
    abstract fun bindUserDetailActivity(): UserDetailActivity
}