package com.mvvmsampleapp

import android.app.Application
import com.mvvmsampleapp.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MVVMSampleApp : Application(),
    HasAndroidInjector {

    //Inject Any
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        DaggerAppComponent.builder().application(this).build()!!.inject(this);
    }

    companion object {
        private lateinit var mInstance: MVVMSampleApp

        @Synchronized
        fun getInstance(): MVVMSampleApp {
            return mInstance
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
}