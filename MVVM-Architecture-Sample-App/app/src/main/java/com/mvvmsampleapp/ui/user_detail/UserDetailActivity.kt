package com.mvvmsampleapp.ui.user_detail

import android.app.Activity
import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mvvmsampleapp.R
import com.mvvmsampleapp.ViewModelProviderFactory
import com.mvvmsampleapp.databinding.ActivityUserDetailBinding
import com.mvvmsampleapp.ui.base.BaseActivity
import javax.inject.Inject

class UserDetailActivity : BaseActivity<ActivityUserDetailBinding, UserDetailViewModel>(),
    UserDetailNavigator {

    @set:Inject
    lateinit var factory: ViewModelProviderFactory
    var mActivity: Activity? = null
    private var mActivityUserDetailBinding: ActivityUserDetailBinding? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_user_detail
    override val viewModel: UserDetailViewModel
        get() = ViewModelProviders.of(this, factory).get(UserDetailViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = this@UserDetailActivity

        mActivityUserDetailBinding = getViewDataBinding()
        viewModel.navigator = this

        val mIntent = intent

        mActivityUserDetailBinding!!.txtName.setText(
            mIntent.getStringExtra("FIRST_NAME") + " " + mIntent.getStringExtra(
                "LAST_NAME"
            )
        )

        mActivityUserDetailBinding!!.txtEmail.setText(mIntent.getStringExtra("EMAIL"))

        Glide
            .with(mActivity as UserDetailActivity)
            .load(mIntent.getStringExtra("AVATAR"))
            .centerCrop()
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(mActivityUserDetailBinding!!.imgAvtar)

    }

    override fun handleError(throwable: Throwable) {
    }
}
