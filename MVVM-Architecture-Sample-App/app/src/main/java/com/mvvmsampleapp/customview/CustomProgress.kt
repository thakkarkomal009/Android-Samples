package com.mvvmsampleapp.customview

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import androidx.core.content.ContextCompat
import com.mvvmsampleapp.R
import com.wang.avi.AVLoadingIndicatorView

class CustomProgress {
    private var mDialog: Dialog? = null

    fun showProgress(context: Context, message: String, cancelable: Boolean) {
        mDialog = Dialog(context)
        // no tile for the dialog
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setContentView(R.layout.custom_progressbar)
        var indicator = mDialog!!.findViewById(R.id.indicator) as AVLoadingIndicatorView
        indicator.setVisibility(View.VISIBLE)
        // you can change or add this line according to your need
        mDialog!!.setCancelable(cancelable)
        mDialog!!.setCanceledOnTouchOutside(cancelable)

        val colorDrawable =
            ColorDrawable(ContextCompat.getColor(context, R.color.transparent))
        mDialog!!.getWindow()!!.setBackgroundDrawable(colorDrawable)

//        indicator.post {
        mDialog!!.show()
//        }
    }

    fun hideProgress(context: Context) {
        if (mDialog != null) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }

    companion object {

        var customProgress: CustomProgress? = null

        val instance: CustomProgress
            get() {
                if (customProgress == null) {
                    customProgress = CustomProgress()
                }
                return customProgress as CustomProgress
            }
    }
}