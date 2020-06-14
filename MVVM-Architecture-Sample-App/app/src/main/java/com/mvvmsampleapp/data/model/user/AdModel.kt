package com.mvvmsampleapp.data.model.user

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class AdModel {
    @SerializedName("company")
    @Expose
    private var company: String? = null

    @SerializedName("url")
    @Expose
    private var url: String? = null

    @SerializedName("text")
    @Expose
    private var text: String? = null

    fun getCompany(): String? {
        return company
    }

    fun setCompany(company: String?) {
        this.company = company
    }

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String?) {
        this.url = url
    }

    fun getText(): String? {
        return text
    }

    fun setText(text: String?) {
        this.text = text
    }
}