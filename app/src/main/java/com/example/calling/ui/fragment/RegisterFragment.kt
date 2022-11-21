package com.example.calling.ui.fragment

import android.annotation.SuppressLint
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
import com.example.calling.databinding.FragmentRegisterBinding
import com.example.calling.model.VolleySingleton
import com.example.calling.utils.Constants
import org.json.JSONException
import org.json.JSONObject

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jsonObject = JSONObject()
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

            //creating volley string request
            val stringRequest = @SuppressLint("ApplySharedPref")
            object : StringRequest(Method.POST, Constants.REGISTER_URL,
                Response.Listener { _ ->
                    try {
                        Toast.makeText(context, "You are register in", Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(volleyError: VolleyError) {
                        Toast.makeText(context, volleyError.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["userID"] = userID
                    params["password"] = password
                    params["adminPassword"] = "sdasd"
                    params["timeLimit"] = "1"
                    return params
                }
            }
            //adding request to queue
            VolleySingleton.instance?.addToRequestQueue(stringRequest)

            //TODO: Retrofit Code Not working
            /*RetrofitClient.instant.userLogin(userID, password)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (!response.body()?.error!!) {
                            PreferenceUtils.saveID(userID, context)
                            Toast.makeText(context, "Login", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(context, MainActivity::class.java))
                            activity?.finish()
                        }else{
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }

                })*/
        }
    }
}