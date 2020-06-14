package com.mvvmsampleapp.ui.user

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.mvvmsampleapp.R
import com.mvvmsampleapp.data.model.user.UserListModel
import com.mvvmsampleapp.data.model.user.UserListResult
import com.mvvmsampleapp.data.repository.MVVMRepository.Companion.apiService
import com.mvvmsampleapp.ui.base.BaseViewModel
import com.mvvmsampleapp.util.rx.SchedulerProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListViewModel(
    schedulerProvider: SchedulerProvider, context: Context
) :
    BaseViewModel<UserListNavigator>(schedulerProvider, context) {
    lateinit var mActivity: Activity

    val userListLiveData: MutableLiveData<ArrayList<UserListResult>> = MutableLiveData()

    fun setContext(mContext: Activity) {
        mActivity = mContext
    }

    fun userListAPICall() {
        setLoading(true, mActivity)
        apiService.getUserList(1, 10)?.enqueue(object : Callback<UserListModel> {
            override fun onFailure(call: Call<UserListModel>, t: Throwable) {
                navigator?.handleError(
                    throwable = Throwable(
                        mActivity.getString(
                            R.string.something_went_wrong
                        )
                    )
                )
            }

            override fun onResponse(call: Call<UserListModel>, response: Response<UserListModel>) {
                setLoading(false, mActivity)
                userListLiveData.value = response.body()!!.getData() as ArrayList<UserListResult>?
            }
        })
    }
}