package com.example.calling.ui.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Toast


class MyReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent?) {
        val mtelephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        mtelephony.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                super.onCallStateChanged(state, incomingNumber)
                when (state) {
                    TelephonyManager.CALL_STATE_RINGING -> {
                        Toast.makeText(
                            context,
                            "I'm in $state and the number is $incomingNumber",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TelephonyManager.CALL_STATE_OFFHOOK -> {
                        Toast.makeText(
                            context,
                            "I'm in $state and the number is $incomingNumber",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    TelephonyManager.CALL_STATE_IDLE -> {
                        Toast.makeText(
                            context,
                            "I'm in $state and the number is $incomingNumber",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE)
    }
}