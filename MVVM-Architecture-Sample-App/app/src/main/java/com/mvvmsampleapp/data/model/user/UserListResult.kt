package com.mvvmsampleapp.data.model.user

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class UserListResult {
    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("email")
    @Expose
    private var email: String? = null

    @SerializedName("first_name")
    @Expose
    private var firstName: String? = null

    @SerializedName("last_name")
    @Expose
    private var lastName: String? = null

    @SerializedName("avatar")
    @Expose
    private var avatar: String? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getFirstName(): String? {
        return firstName
    }

    fun setFirstName(firstName: String?) {
        this.firstName = firstName
    }

    fun getLastName(): String? {
        return lastName
    }

    fun setLastName(lastName: String?) {
        this.lastName = lastName
    }

    fun getAvatar(): String? {
        return avatar
    }

    fun setAvatar(avatar: String?) {
        this.avatar = avatar
    }
}