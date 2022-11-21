package com.example.calling.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.calling.adapter.CallLogsAdapter
import com.example.calling.databinding.ActivityCallLogsBinding
import com.example.calling.model.CallLogs
import com.example.calling.utils.Constants
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

class CallLogsActivity : AppCompatActivity() {

    lateinit var binding: ActivityCallLogsBinding
    var mobile = ""
    private lateinit var callLogsAdapter: CallLogsAdapter
    private lateinit var callLogsArrayList: ArrayList<CallLogs>
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallLogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent.extras
        if (intent != null) {
            mobile = intent.getString("MOBILE").toString()
        }
        callLogsArrayList = ArrayList()
        getCallLogs()
    }

    private fun getCallLogs() {
        val request: StringRequest =
            object : StringRequest(Method.POST, Constants.GET_PHONE_LOG, object :
                Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    try {
                        val json = response.toString()
                        val dataJson = JSONObject(json)
                        val error = dataJson.getString("error")
                        //val message = dataJson.getString("message")
                        if (error.equals("false")) {
                            val jsonArray = dataJson.getJSONArray("callLogs")
                            for (i in 0 until jsonArray.length()) {
                                try {
                                    val jsonObject = jsonArray.getJSONObject(i)
                                    val callLogs = CallLogs(
                                        jsonObject.getString("mobile"),
                                        jsonObject.getString("date"),
                                        jsonObject.getString("time"),
                                        jsonObject.getString("action")
                                    )
                                    callLogsArrayList.add(callLogs)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                            fillDataInRecycler()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }, Response.ErrorListener { volleyError ->
                Toast.makeText(
                    applicationContext,
                    "error!" + volleyError.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["mobile"] = mobile
                    return params
                }
            }

        val requestQueue = Volley.newRequestQueue(applicationContext)
        requestQueue.add(request)
    }

    private fun fillDataInRecycler() {
        callLogsAdapter = CallLogsAdapter(callLogsArrayList)
        recyclerView = binding.recyclerViewCallLogs
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = callLogsAdapter
        recyclerView.setHasFixedSize(true)
    }
}