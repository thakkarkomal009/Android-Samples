package com.mvvmsampleapp.data.model.user

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class UserListModel {
    @SerializedName("page")
    @Expose
    private var page: Int? = null

    @SerializedName("per_page")
    @Expose
    private var perPage: Int? = null

    @SerializedName("total")
    @Expose
    private var total: Int? = null

    @SerializedName("total_pages")
    @Expose
    private var totalPages: Int? = null

    @SerializedName("data")
    @Expose
    private var data: List<UserListResult?>? = null

    @SerializedName("ad")
    @Expose
    private var ad: AdModel? = null

    fun getPage(): Int? {
        return page
    }

    fun setPage(page: Int?) {
        this.page = page
    }

    fun getPerPage(): Int? {
        return perPage
    }

    fun setPerPage(perPage: Int?) {
        this.perPage = perPage
    }

    fun getTotal(): Int? {
        return total
    }

    fun setTotal(total: Int?) {
        this.total = total
    }

    fun getTotalPages(): Int? {
        return totalPages
    }

    fun setTotalPages(totalPages: Int?) {
        this.totalPages = totalPages
    }

    fun getData(): List<UserListResult?>? {
        return data
    }

    fun setData(data: List<UserListResult?>?) {
        this.data = data
    }

    fun getAd(): AdModel? {
        return ad
    }

    fun setAd(ad: AdModel?) {
        this.ad = ad
    }
}