package com.mvvmsampleapp.ui.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.mvvmsampleapp.customview.CustomProgress
import com.mvvmsampleapp.util.rx.SchedulerProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>(
    val schedulerProvider: SchedulerProvider, val context: Context
) : ViewModel() {

    var isLoading = ObservableBoolean()
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var mNavigator: WeakReference<N>? = null
    var mProgressDialog: ProgressDialog? = null

    var navigator: N?
        get() = mNavigator?.get()
        set(navigator) {
            this.mNavigator = WeakReference<N>(navigator)!!
        }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }


    fun setLoading(b: Boolean, mContext: Activity) {
        this.isLoading.set(b)
        showProgressDialog(b, mContext)
    }

    fun showProgressDialog(loading: Boolean, context: Context) {

        var customProgress: CustomProgress = CustomProgress.instance

        if (loading) {
            customProgress.showProgress(context, "", false)
        } else {
            customProgress.hideProgress(context)
        }
    }
}