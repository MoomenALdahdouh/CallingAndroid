package com.example.calling.ui.activity

import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Toast
import com.example.calling.utils.Constants

class PhoneStateChangeListener : PhoneStateListener() {
    var count = -1
    var cutting = false
    override fun onCallStateChanged(state: Int, incomingNumber: String) {
        Constants.CUTTING = false
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                Toast.makeText(
                    context,
                    "I'm in $state and the number is $incomingNumber",
                    Toast.LENGTH_SHORT
                ).show()
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                Constants.CUTTING = true
                Toast.makeText(
                    context,
                    "I'm in $state and the number is $incomingNumber",
                    Toast.LENGTH_SHORT
                ).show()
            }
            TelephonyManager.CALL_STATE_IDLE -> {
                //count++
                if (cutting) {
                    Constants.CUTTING = false

                    /*val request: StringRequest =
                        object : StringRequest(Method.POST, Constants.UPDATE_USER_URL, object :
                            Response.Listener<String?> {
                            override fun onResponse(response: String?) {
                                try {
                                    val json = response.toString()
                                    val dataJson = JSONObject(json)
                                    val error = dataJson.getString("error")
                                    val message = dataJson.getString("message")
                                    val user = dataJson.getJSONObject("user")
                                    val userid = user.getString("userID")
                                    val time = user.getString("time")
                                    val balance = user.getString("balance")
                                    val adminNum = user.getString("admno")

                                    if (error.equals("false")) {
                                        PreferenceUtils.saveID(userid, context)
                                        PreferenceUtils.saveTime(time, context)
                                        PreferenceUtils.saveBalance(balance, context)
                                        PreferenceUtils.saveAdminNum(adminNum, context)
                                    }
                                    Toast.makeText(context, "$message", Toast.LENGTH_SHORT)
                                        .show()
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                        }, object : Response.ErrorListener {
                            override fun onErrorResponse(volleyError: VolleyError) {
                                Toast.makeText(context, "error!" + volleyError.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }) {
                            @Throws(AuthFailureError::class)
                            override fun getParams(): Map<String, String> {
                                val params = HashMap<String, String>()
                                params["userID"] = userID
                                params["time"] = time
                                params["balance"] = balance
                                return params
                            }
                        }

                    val requestQueue = Volley.newRequestQueue(context)
                    requestQueue.add(request)*/
                }
            }
        }
    }

    var context: Context? = null
    /*var userID = PreferenceUtils.getID(context)
    var time = PreferenceUtils.getTime(context)
    var balance = PreferenceUtils.getBalance(context)*/
}