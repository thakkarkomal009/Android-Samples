package com.mvvmsampleapp.di.component

import android.app.Application
import com.mvvmsampleapp.MVVMSampleApp
import com.mvvmsampleapp.di.builder.ActivityBuilder
import com.mvvmsampleapp.di.builder.FragmentBuilder
import com.mvvmsampleapp.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityBuilder::class, FragmentBuilder::class])
interface AppComponent {
    fun inject(app: MVVMSampleApp?)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent?
    }
}