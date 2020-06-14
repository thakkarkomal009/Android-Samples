package com.mvvmsampleapp.ui.login

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import com.mvvmsampleapp.R
import com.mvvmsampleapp.ViewUtils
import com.mvvmsampleapp.ViewUtils.errorBodyToMessage
import com.mvvmsampleapp.data.model.LoginModel
import com.mvvmsampleapp.data.repository.MVVMRepository.Companion.apiService
import com.mvvmsampleapp.ui.base.BaseViewModel
import com.mvvmsampleapp.util.rx.SchedulerProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    schedulerProvider: SchedulerProvider,
    context: Context
) : BaseViewModel<LoginNavigator>(schedulerProvider, context) {
    lateinit var mActivity: Activity

    fun setContext(mContext: Activity) {
        mActivity = mContext
    }

    fun onLoginClick() {
        navigator?.login()
    }

    fun isEmailAndPasswordValid(
        email: String?,
        password: String?
    ): Boolean {
        // validate email and password
        if (TextUtils.isEmpty(email)) {
            navigator?.handleError(throwable = Throwable(mActivity.getString(R.string.enter_email)))
            return false
        }
        if (!ViewUtils.isEmailValid(email)) {
            navigator?.handleError(throwable = Throwable(mActivity.getString(R.string.invalid_email)))
            return false
        }
        return if (TextUtils.isEmpty(password)) {
            navigator?.handleError(throwable = Throwable(mActivity.getString(R.string.enter_password)))
            false
        } else true
    }

    fun login(email: String, password: String) {
        setLoading(true, mActivity)
        apiService.doLogin(email, password)?.enqueue(object :
            Callback<LoginModel> {
            override fun onResponse(
                call: Call<LoginModel>,
                response: Response<LoginModel>
            ) {
                setLoading(false, mActivity)
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        navigator?.signInSuccess(mActivity.getString(R.string.login_Success))
                    } else {
                        navigator?.handleError(
                            throwable = Throwable(
                                errorBodyToMessage(
                                    response.errorBody()!!.string()
                                )
                            )
                        )
                    }
                } else {
                    setLoading(false, mActivity)
                    navigator?.handleError(
                        throwable = Throwable(
                            errorBodyToMessage(
                                response.errorBody()!!.string()
                            )
                        )
                    )
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                setLoading(false, mActivity)
                navigator?.handleError(throwable = Throwable(t.message))
            }
        })
    }
}