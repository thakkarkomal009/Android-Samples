package com.mvvmsampleapp.di.module

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mvvmsampleapp.data.remote.ApiHelper
import com.mvvmsampleapp.data.repository.MVVMRepository
import com.mvvmsampleapp.util.rx.AppSchedulerProvider
import com.mvvmsampleapp.util.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideApiHelper(appApiHelper: MVVMRepository): ApiHelper {
        return appApiHelper
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()
}