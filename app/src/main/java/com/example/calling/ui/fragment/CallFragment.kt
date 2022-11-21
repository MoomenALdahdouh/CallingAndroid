package com.example.calling.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.calling.R
import com.example.calling.adapter.ContactsAdapter
import com.example.calling.databinding.FragmentCallBinding
import com.example.calling.model.Contact
import com.example.calling.ui.activity.*
import com.example.calling.utils.Constants
import com.example.calling.utils.PreferenceUtils
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class CallFragment : Fragment() {
    private lateinit var binding: FragmentCallBinding
    private lateinit var contactsAdapter: ContactsAdapter
    private lateinit var contactArrayList: ArrayList<Contact>
    private lateinit var recyclerView: RecyclerView
    private val REQUEST_CALL = 1
    private val REQUEST_READ_LOG = 2
    private var mobile: String? = null
    val MOBILE = "MOBILE"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactArrayList = ArrayList()
        getAllContacts(contactArrayList)
        userInfo()
        getCallLogs()
        exitApp()
        logoutApp()
        binding.buttonVisitSite.setOnClickListener{
            startActivity(Intent(context, WebViewActivity::class.java))
        }
    }

    private fun getCallLogs() {
        /*Get Logs*/
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it, Manifest.permission.READ_CALL_LOG
                )
            } != PackageManager.PERMISSION_GRANTED
            || context?.let {
                ContextCompat.checkSelfPermission(
                    it, Manifest.permission.READ_CONTACTS
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS),
                REQUEST_READ_LOG
            )
        } /*else {
            getCallDetails()
        }*/
    }

    private fun userInfo() {
        binding.textViewUserId.text = PreferenceUtils.getID(context)
        binding.textViewUserBal.text = PreferenceUtils.getBalance(context)
        binding.textViewUserLimit.text = PreferenceUtils.getTime(context)
    }

    private fun calling() {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it, Manifest.permission.CALL_PHONE
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL
            )
        } else {
            val dial = "tel:$mobile"
            if (!PreferenceUtils.getBalance(context).equals("0")) {
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        }
    }

    var phNumber = ""
    var callType = ""
    var callDate = ""
    var callDayTime: Date? = null
    var callDuration = ""
    var admno = ""
    var dateCall: String = ""
    var timeCall: String = ""
    var dateCutt: String = ""
    var timeCutt: String = ""

    @SuppressLint("UseRequireInsteadOfGet", "SimpleDateFormat")
    private fun getCallDetails() {
        try {
            val sb = StringBuffer()
            val cursor =
                context!!.contentResolver.query(
                    CallLog.Calls.CONTENT_URI,
                    null,
                    null,
                    null,
                    CallLog.Calls.DATE + " DESC limit 1"
                )
            val number = cursor!!.getColumnIndex(CallLog.Calls.NUMBER)
            val type = cursor.getColumnIndex(CallLog.Calls.TYPE)
            val date = cursor.getColumnIndex(CallLog.Calls.DATE)
            val duration = cursor.getColumnIndex(CallLog.Calls.DURATION)
            sb.append("Call Details :")
            while (cursor.moveToNext()) {
                phNumber = cursor.getString(number)
                callType = cursor.getString(type)
                callDuration = cursor.getString(duration)

                callDate = cursor.getString(date)
                callDayTime = Date(java.lang.Long.valueOf(callDate))
                //Calling date and time
                val formatDate = SimpleDateFormat("dd-MMM-yyyy")
                val formatTime = SimpleDateFormat("HH:mm:ss")
                dateCall = formatDate.format(Date(callDate.toLong()))
                timeCall = formatTime.format(Date(callDate.toLong()))

                //Cutting date and time
                val currentTime = Calendar.getInstance().time
                dateCutt = formatDate.format(currentTime)
                timeCutt = formatTime.format(currentTime)

                var dir: String? = null
                when (callType.toInt()) {
                    CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING"
                    CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING"
                    CallLog.Calls.MISSED_TYPE -> dir = "MISSED"
                }
                sb.append("\nPhone Number:--- $phNumber \nCall Type:--- $dir \nCall Date:--- $callDayTime \nCall duration in sec :--- $callDuration")
                sb.append("\n----------------------------------")
            }
            cursor.close()
            //val call = sb
            //binding.textView4.text = sb
            admno = PreferenceUtils.getAdminNum(context).toString()
        } catch (e: SecurityException) {

        }

    }

    private fun registerCallLog(
        admno: String,
        phNumber: String,
        date: String,
        action: String,
        time: String
    ) {
        val request: StringRequest =
            object : StringRequest(Method.POST, Constants.ADD_PHONE_LOG,
                Response.Listener<String?> { response ->
                    try {
                        val json = response.toString()
                        val dataJson = JSONObject(json)
                        val error = dataJson.getString("error")
                        val message = dataJson.getString("message")
                        Toast.makeText(context, "$message", Toast.LENGTH_SHORT)
                            .show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { volleyError ->
                    Toast.makeText(context, "error!" + volleyError.message, Toast.LENGTH_SHORT)
                        .show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["admno"] = admno
                    params["mobile"] = phNumber
                    params["date"] = date
                    params["time"] = time
                    params["action"] = action
                    return params
                }
            }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }

    private fun getAllContacts(contactArrayList: ArrayList<Contact>) {
        val request = JsonObjectRequest(Constants.GET_PHONE_CALL_URL,
            { response ->
                val jsonArray = response.getJSONArray("contacts")
                for (i in 0 until jsonArray.length()) {
                    try {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val contact = Contact(
                            jsonObject.getInt("id"),
                            jsonObject.getInt("admno"),
                            jsonObject.getString("type"),
                            jsonObject.getString("phone"),
                            jsonObject.getString("remark")
                        )
                        contactArrayList.add(contact)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                fillDataInRecycler()
            }) { Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show() }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }

    private fun fillDataInRecycler() {
        contactsAdapter = ContactsAdapter(contactArrayList)
        contactsAdapter.onItemSetOnClickListener(object : ContactsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, itemId: Int) {
                if (itemId == R.id.imageView_call) {
                    mobile = contactArrayList[position].phone.toString()
                    calling()
                } else if (itemId == R.id.contact_item_id) {
                    var intent = Intent(context, CallLogsActivity::class.java)
                    mobile = contactArrayList[position].phone.toString()
                    intent.putExtra("MOBILE", mobile)
                    startActivity(intent)

                }
            }
        })
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = contactsAdapter
        recyclerView.setHasFixedSize(true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /*if (requestCode == REQUEST_READ_LOG)
            getCallDetails()*/
    }

    override fun onResume() {
        super.onResume()
        val pscl = PhoneStateChangeListener()
        pscl.context = context
        val tm = requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        tm.listen(pscl, PhoneStateListener.LISTEN_CALL_STATE)
        if (Constants.CUTTING) {
            Toast.makeText(
                context,
                "I'm cutting",
                Toast.LENGTH_SHORT
            ).show()
            updateUser()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateUser() {
        getCallDetails()

        val userID = PreferenceUtils.getID(context).toString()
        val duraction = Integer.parseInt(callDuration) / 60
        var time = Integer.parseInt(PreferenceUtils.getTime(context).toString().trim())
        var balance = Integer.parseInt(PreferenceUtils.getBalance(context).toString().trim())
        if (balance > 0) {
            time -= duraction
            balance -= duraction
            /*PreferenceUtils.saveTime("" + time, context)
            PreferenceUtils.saveBalance("" + balance, context)*/
            saveUpdate(userID, "" + time, "" + balance)

            registerCallLog(admno, phNumber, dateCall, "called", timeCall)
            registerCallLog(admno, phNumber, dateCutt, "cut", timeCutt)
        } else
            Toast.makeText(context, "Your Balance is 0!", Toast.LENGTH_SHORT)
                .show()
    }

    private fun saveUpdate(userID: String, newTime: String, newBalance: String) {
        val request: StringRequest =
            object : StringRequest(Method.POST, Constants.UPDATE_USER_URL, object :
                Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    try {
                        val json = response.toString()
                        val dataJson = JSONObject(json)
                        val error = dataJson.getString("error")
                        val message = dataJson.getString("message")

                        if (error.equals("false")) {
                            val user = dataJson.getJSONObject("user")
                            val userid = user.getString("userID")
                            val time = user.getString("time")
                            val balance = user.getString("balance")
                            val adminNum = user.getString("admno")
                            PreferenceUtils.saveID(userid, context)
                            PreferenceUtils.saveTime(time, context)
                            PreferenceUtils.saveBalance(balance, context)
                            PreferenceUtils.saveAdminNum(adminNum, context)
                            userInfo()
                        }
                        Toast.makeText(context, "$message", Toast.LENGTH_SHORT)
                            .show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }, Response.ErrorListener { volleyError ->
                Toast.makeText(context, "error!" + volleyError.message, Toast.LENGTH_SHORT)
                    .show()
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["userID"] = userID
                    params["time"] = newTime
                    params["balance"] = newBalance
                    return params
                }
            }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }

    private fun exitApp() {
        binding.buttonExit.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, ExitActivity::class.java))
        })
    }

    private fun logoutApp() {
        binding.buttonSignOut.setOnClickListener(View.OnClickListener {
            PreferenceUtils.saveAdminNum("",context);
            PreferenceUtils.saveID("",context);
            PreferenceUtils.saveBalance("",context);
            PreferenceUtils.saveTime("",context);
            startActivity(Intent(context, SplashActivity::class.java))
            activity?.finish()
        })
    }

}