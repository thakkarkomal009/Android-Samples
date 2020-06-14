package com.mvvmsampleapp.ui.callback

import com.mvvmsampleapp.data.model.user.UserListResult

interface ItemClickListener {
    fun onItemClick(userList: UserListResult?)
}