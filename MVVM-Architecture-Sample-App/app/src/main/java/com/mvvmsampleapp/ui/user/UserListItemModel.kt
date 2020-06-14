package com.mvvmsampleapp.ui.user

import androidx.databinding.ObservableField
import com.mvvmsampleapp.data.model.user.UserListResult
import com.mvvmsampleapp.ui.callback.ItemClickListener

class UserListItemModel(
    private val userListResult: UserListResult,
    var itemClickListener: ItemClickListener
) {
    var userId: ObservableField<Int> = ObservableField(userListResult.getId()!!)
    var firstName: ObservableField<String> = ObservableField(userListResult.getFirstName()!!)
    var lastName: ObservableField<String> = ObservableField(userListResult.getLastName()!!)
    var email: ObservableField<String> = ObservableField(userListResult.getEmail()!!)
    var imageUrl: ObservableField<String> = ObservableField(userListResult.getAvatar()!!)

    fun onItemClick() {
        itemClickListener.onItemClick(userListResult)
    }
}