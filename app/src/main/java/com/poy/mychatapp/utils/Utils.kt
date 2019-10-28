package com.poy.mychatapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


object Utils {

    fun closeKeyboard(context: Context, view: View) {
        val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    fun saveLocalUser(context: Context, username: String, email: String, id: String) {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(Constants.PREFERENCES_USER_NAME, username)
            .putString(Constants.PREFERENCES_USER_EMAIL, email)
            .putString(Constants.PREFERENCES_USER_ID, id)
            .apply()

    }

    fun getLocalUsername(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getString(Constants.PREFERENCES_USER_NAME, Constants.DEFAULT_USER)
    }

    fun getLocalUserId(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getString(Constants.PREFERENCES_USER_ID, Constants.DEFAULT_ID)
    }
}