package com.poy.mychatapp.utils

interface Constants {
    companion object {

        val SHARED_PREFERENCES = "APP_PREFS"

        val PREFERENCES_USER_NAME = "username"
        val PREFERENCES_USER_EMAIL = "email"
        val PREFERENCES_USER_ID = "id"

        val DATABASE_NAME = "chat"

        val LOG_TAG = "FirebaseChat"

        val DEFAULT_USER = "User"
        val DEFAULT_ID = "0000"
    }
}