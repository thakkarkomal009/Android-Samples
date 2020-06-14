package com.mvvmsampleapp

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.Patterns
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import org.json.JSONObject

object ViewUtils {

    fun isEmailValid(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun errorBodyToMessage(response: String): String {
        var jObjError: JSONObject = JSONObject(response)
        return jObjError.getString("error")
    }

    fun ShowValidation(mActivity: Activity, mTextView: TextView, message: String) {
        var animShow: Animation
        var animHide: Animation
        animShow = AnimationUtils.loadAnimation(mActivity, R.anim.anim_view_show)
        animHide = AnimationUtils.loadAnimation(mActivity, R.anim.anim_view_hide)

        mTextView.text = message
        mTextView.visibility = View.VISIBLE
        mTextView.startAnimation(animShow)
        Handler().postDelayed({
            mTextView.startAnimation(animHide)
            mTextView.visibility = View.GONE
        }, 2000)
    }

    fun hideKeyboard(mContext: Context, mView: View) {
        val mInputMethodManager =
            mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mInputMethodManager.hideSoftInputFromWindow(mView.windowToken, 0)
    }
}