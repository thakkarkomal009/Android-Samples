package com.mvvmsampleapp.ui.login

import android.app.Activity
import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.mvvmsampleapp.R
import com.mvvmsampleapp.ViewModelProviderFactory
import com.mvvmsampleapp.ViewUtils
import com.mvvmsampleapp.ViewUtils.hideKeyboard
import com.mvvmsampleapp.databinding.ActivityLoginBinding
import com.mvvmsampleapp.ui.base.BaseActivity
import com.mvvmsampleapp.ui.user.UserListActivity
import javax.inject.Inject

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(),
    LoginNavigator {

    @set:Inject
    lateinit var factory: ViewModelProviderFactory
    var mActivity: Activity? = null
    private var mActivityLoginBinding: ActivityLoginBinding? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_login
    override val viewModel: LoginViewModel
        get() = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = this@LoginActivity

        viewModel.setContext(mActivity as LoginActivity)
        mActivityLoginBinding = getViewDataBinding()
        viewModel.navigator = this
    }

    override fun login() {
        hideKeyboard(this!!.mActivity!!, mActivityLoginBinding!!.txtLogin)
        val email = mActivityLoginBinding!!.edtEmail.text.toString()
        val password = mActivityLoginBinding!!.edtPassword.text.toString()
        if (viewModel.isEmailAndPasswordValid(email, password)) {
            viewModel.login(email, password)
        }
    }

    override fun signInSuccess(message: String) {
        handleError(throwable = Throwable(message))
        mActivity!!.launchWithFinishActivity<UserListActivity> { }
    }

    override fun handleError(throwable: Throwable) {
        ViewUtils.ShowValidation(
            this!!.mActivity!!,
            mActivityLoginBinding!!.txtSnackBar,
            throwable.message.toString()
        )
    }
}