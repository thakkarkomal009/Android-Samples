package com.mvvmsampleapp.ui.base

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dagger.android.AndroidInjection

abstract class BaseActivity<T : ViewDataBinding?, V : BaseViewModel<*>?> : AppCompatActivity(),
    BaseFragment.Callback {
    private var mViewDataBinding: T? = null
    private var mViewModel: V? = null

    abstract val bindingVariable: Int

    @get:LayoutRes
    abstract val layoutId: Int

    abstract val viewModel: V

    override fun onFragmentAttached() {}
    override fun onFragmentDetached(tag: String?) {}
    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    open fun getViewDataBinding(): T {
        return this!!.mViewDataBinding!!
    }

    fun performDependencyInjection() {
        AndroidInjection.inject(this)
    }

    private fun performDataBinding() {
        this.mViewDataBinding = DataBindingUtil.setContentView<T>(this, layoutId)
        this.mViewModel = if (mViewModel == null) viewModel else mViewModel
        if (mViewModel == null) getViewDataBinding()!!.setVariable(
            bindingVariable,
            viewModel
        ) else getViewDataBinding()!!.setVariable(bindingVariable, mViewModel)
        getViewDataBinding()!!.setVariable(bindingVariable, viewModel)
        getViewDataBinding()!!.executePendingBindings()
    }

    inline fun <reified T : Any> newIntent(context: Context): Intent =
        Intent(context, T::class.java)

    inline fun <reified T : Any> Context.launchWithFinishActivity(
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
    ) {

        val intent = newIntent<T>(this)
        intent.init()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options)
        } else {
            startActivity(intent)
        }
        finish()
    }

    inline fun <reified T : Any> Context.launchActivity(
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}
    ) {

        val intent = newIntent<T>(this)
        intent.init()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options)
        } else {
            startActivity(intent)
        }
    }

}