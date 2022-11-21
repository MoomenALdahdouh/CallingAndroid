package com.example.calling.ui.activity

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast


class PhoneState : Service() {
    private val mCallStateListener: CallStateListener = CallStateListener()
    private var mTelephonyManager: TelephonyManager? = null
    private var mCallState = 0
    override fun onCreate() {
        super.onCreate()
        mTelephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager?
        mCallState = mTelephonyManager!!.callState
        mTelephonyManager!!.listen(mCallStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun onDestroy() {
        Log.d("onDestroy", "onDestroy")
        mTelephonyManager!!.listen(mCallStateListener, PhoneStateListener.LISTEN_NONE)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null //-- not a bound service--
    }

    private inner class CallStateListener : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            when (mCallState) {
                TelephonyManager.CALL_STATE_IDLE -> if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    Log.d("state", "idle --> off hook = new outgoing call")
                    Toast.makeText(
                        applicationContext,
                        "I'm in $state and the number is $incomingNumber",
                        Toast.LENGTH_SHORT
                    ).show()
                    // idle --> off hook = new outgoing call
                    //triggerSenses(Sense.CallEvent.OUTGOING);
                } else if (state == TelephonyManager.CALL_STATE_RINGING) {
                    Log.d("state", "idle --> ringing = new incoming call")
                    // idle --> ringing = new incoming call
                    //triggerSenses(Sense.CallEvent.INCOMING);
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    if (state == TelephonyManager.CALL_STATE_IDLE) {
                        Toast.makeText(
                            applicationContext,
                            "I'm in $state and the number is $incomingNumber",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("state", "off hook --> idle  = disconnected")
                        // off hook --> idle  = disconnected
                        //triggerSenses(Sense.CallEvent.ENDED);
                    } else if (state == TelephonyManager.CALL_STATE_RINGING) {
                        Log.d("state", "off hook --> ringing = another call waiting")
                        // off hook --> ringing = another call waiting
                        //triggerSenses(Sense.CallEvent.WAITING);
                    }
                    Log.d("CALL_STATE_OFFHOOK", state.toString())
                }
                TelephonyManager.CALL_STATE_RINGING -> if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    Log.d("state", "ringing --> off hook = received")
                    // ringing --> off hook = received
                    //triggerSenses(Sense.CallEvent.RECEIVED);
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    Log.d("state", "ringing --> idle = missed call")
                    // ringing --> idle = missed call
                    //triggerSenses(Sense.CallEvent.MISSED);
                }
            }
            mCallState = state
        }
    }

    companion object {
        fun init(c: Context) {
            c.startService(Intent(c, PhoneState::class.java))
            Log.d("Service enabled", "Service enabled: " + true)
        }
    }
}