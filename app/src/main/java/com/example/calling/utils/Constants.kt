package com.example.calling.utils

import android.util.Base64

object Constants {
    const val KEY_ID = "ID"
    const val KEY_BALANCE = "KEY_BALANCE"
    const val KEY_TIME = "KEY_TIME"
    const val KEY_ADMIN_NUM = "KEY_ADMIN_NUM"
    var CUTTING = false

    const val BASE_URL = "http://192.168.137.1/dett/v1/"
    //const val BASE_URL = "https://dett.moomena.com/v1/"
    const val LOGIN_URL = BASE_URL + "loginUser.php"
    const val REGISTER_URL = BASE_URL + "registerUser.php"
    const val GET_PHONE_CALL_URL = BASE_URL + "getContacts.php"
    const val UPDATE_USER_URL = BASE_URL + "updateUser.php"
    const val ADD_PHONE_LOG = BASE_URL + "addPhoneLog.php"
    const val GET_PHONE_LOG = BASE_URL + "getContactsLogs.php"
    const val CHECK_ADMIN_PASSWORD = BASE_URL + "checkAdmin.php"

    //val AUTH = "Basic" + Base64.encodeToString("moomen:9124279".toByteArray(), Base64.NO_WRAP)
    val AUTH = "Basic" + Base64.encodeToString(
        "shiks9gc_api:Api@shikshasolutions".toByteArray(),
        Base64.NO_WRAP
    )

}