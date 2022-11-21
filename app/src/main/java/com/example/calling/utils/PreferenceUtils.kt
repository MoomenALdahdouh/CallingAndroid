package com.example.calling.utils

import android.content.Context
import android.preference.PreferenceManager

object PreferenceUtils {
    fun saveID(userId: String?, context: Context?): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(Constants.KEY_ID, userId)
        editor.apply()
        return true
    }

    fun getID(context: Context?): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(Constants.KEY_ID, null)
    }

    fun saveTime(name: String?, context: Context?): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(Constants.KEY_TIME, name)
        editor.apply()
        return true
    }

    fun getTime(context: Context?): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(Constants.KEY_TIME, null)
    }

    fun saveBalance(userUid: String?, context: Context?): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(Constants.KEY_BALANCE, userUid)
        editor.apply()
        return true
    }

    fun getBalance(context: Context?): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(Constants.KEY_BALANCE, null)
    }

    fun saveAdminNum(userUid: String?, context: Context?): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(Constants.KEY_ADMIN_NUM, userUid)
        editor.apply()
        return true
    }

    fun getAdminNum(context: Context?): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(Constants.KEY_ADMIN_NUM, null)
    }

}