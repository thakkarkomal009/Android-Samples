package com.mvvmsampleapp.ui.user

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mvvmsampleapp.MVVMSampleApp
import com.mvvmsampleapp.data.model.user.UserListResult
import com.mvvmsampleapp.databinding.UserListItemBinding
import com.mvvmsampleapp.ui.base.BaseViewHolder
import com.mvvmsampleapp.ui.callback.ItemClickListener
import com.mvvmsampleapp.ui.user_detail.UserDetailActivity
import javax.inject.Inject

class UserListAdapter @Inject constructor() :
    RecyclerView.Adapter<BaseViewHolder>() {

    var userListList: ArrayList<UserListResult> = ArrayList()

    override fun getItemCount(): Int {
        return userListList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return UserListViewHolder(
            UserListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun addUsers(repoList: List<UserListResult>) {
        userListList.addAll(repoList)
        notifyDataSetChanged()
    }

    inner class UserListViewHolder(private val mBinding: UserListItemBinding) :
        BaseViewHolder(mBinding.root), ItemClickListener {

        override fun onBind(position: Int) {
            mBinding.viewModel = UserListItemModel(userListList[position], this)
            mBinding.executePendingBindings()
        }

        override fun onItemClick(userList: UserListResult?) {
            var mIntent: Intent = Intent(
                MVVMSampleApp.getInstance().applicationContext,
                UserDetailActivity::class.java
            )
            mIntent.putExtra("FIRST_NAME", userList!!.getFirstName())
            mIntent.putExtra("LAST_NAME", userList!!.getLastName())
            mIntent.putExtra("AVATAR", userList!!.getAvatar())
            mIntent.putExtra("EMAIL", userList!!.getEmail())
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            MVVMSampleApp.getInstance().applicationContext.startActivity(mIntent)
        }
    }

    object DataBindingAdapter {
        @BindingAdapter("android:src")
        @JvmStatic
        fun setImageByRes(imageView: ImageView, resId: String?) {

            Glide
                .with(imageView.getContext())
                .load(resId)
                .centerCrop()
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }
    }
}