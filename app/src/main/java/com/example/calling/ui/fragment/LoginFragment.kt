package com.example.calling.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.calling.databinding.FragmentLoginBinding
import com.example.calling.ui.activity.ExitActivity
import com.example.calling.ui.activity.SplashActivity
import com.example.calling.ui.activity.WebViewActivity
import com.example.calling.utils.Constants
import com.example.calling.utils.PreferenceUtils
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set
import kotlin.math.log


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exitApp()
        login()
        binding.buttonVisitSite.setOnClickListener {
            startActivity(Intent(context, WebViewActivity::class.java))
        }
    }

    private fun login() {
        binding.buttonLoginId.setOnClickListener {
            val userID = binding.editTextUserID.text.toString().trim()
            val password = binding.editTextUserPassword.text.toString().trim()
            if (userID.isEmpty()) {
                binding.buttonLoginId.error = "User ID is required"
                binding.buttonLoginId.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.editTextUserPassword.error = "password is required"
                binding.editTextUserPassword.requestFocus()
                return@setOnClickListener
            }

            val request: StringRequest =
                object : StringRequest(Method.POST, Constants.LOGIN_URL, object :
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
                                binding.textViewWrong.visibility = View.GONE
                                PreferenceUtils.saveID(userid, context)
                                PreferenceUtils.saveTime(time, context)
                                PreferenceUtils.saveBalance(balance, context)
                                PreferenceUtils.saveAdminNum(adminNum, context)
                                startActivity(Intent(context, SplashActivity::class.java))
                                activity?.finish()
                            } else {
                                binding.textViewWrong.text = message
                                binding.textViewWrong.visibility = View.VISIBLE
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
                        params["password"] = password
                        return params
                    }
                }
            val requestQueue = Volley.newRequestQueue(context)
            requestQueue.add(request)
        }

    }

    private fun exitApp() {
        binding.buttonExit.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context, ExitActivity::class.java))
        })
    }
}