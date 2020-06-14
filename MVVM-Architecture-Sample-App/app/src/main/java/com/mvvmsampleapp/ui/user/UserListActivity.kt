package com.mvvmsampleapp.ui.user

import android.app.Activity
import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import com.mvvmsampleapp.R
import com.mvvmsampleapp.ViewModelProviderFactory
import com.mvvmsampleapp.data.model.user.UserListResult
import com.mvvmsampleapp.databinding.ActivityUserListBinding
import com.mvvmsampleapp.ui.base.BaseActivity
import com.mvvmsampleapp.ui.callback.ItemClickListener
import com.mvvmsampleapp.ui.login.LoginActivity
import com.mvvmsampleapp.ui.user_detail.UserDetailActivity
import javax.inject.Inject

class UserListActivity : BaseActivity<ActivityUserListBinding, UserListViewModel>(),
    UserListNavigator, ItemClickListener {

    @Inject
    lateinit var mUserListAdapter: UserListAdapter

    @set:Inject
    lateinit var factory: ViewModelProviderFactory
    var mActivity: Activity? = null
    private var mActivityUserListBinding: ActivityUserListBinding? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_user_list
    override val viewModel: UserListViewModel
        get() = ViewModelProviders.of(this, factory).get(UserListViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = this@UserListActivity

        viewModel.setContext(mActivity as UserListActivity)
        mActivityUserListBinding = getViewDataBinding()
        viewModel.navigator = this

        viewModel.userListAPICall()

        //Bind RecyclerView with Adapter
        mUserListAdapter = UserListAdapter()
        mActivityUserListBinding?.recyclerUserList?.itemAnimator = DefaultItemAnimator()
        mActivityUserListBinding?.recyclerUserList?.adapter = mUserListAdapter

        viewModel.userListLiveData.observe(this,
            Observer<List<UserListResult>> { unit ->
                if (unit.isNotEmpty()) {
                    mUserListAdapter.addUsers(unit as List<UserListResult>)
                }
            })
    }

    override fun handleError(throwable: Throwable) {
    }

    override fun onItemClick(userList: UserListResult?) {
        launchActivity<UserDetailActivity> {
            putExtra("FIRST_NAME", userList!!.getFirstName())
            putExtra("LAST_NAME", userList!!.getLastName())
            putExtra("AVATAR", userList!!.getAvatar())
        }
    }
}