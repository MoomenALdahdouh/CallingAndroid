package com.example.calling.ui.activity

import android.Manifest
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.calling.R
import com.example.calling.databinding.ActivityMainBinding
import com.example.calling.ui.fragment.CallFragment
import com.example.calling.ui.fragment.LoginFragment
import com.example.calling.utils.Constants
import com.example.calling.utils.PreferenceUtils
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragment: Fragment
    private val fragmentManager = supportFragmentManager
    private val fragmentTransaction = fragmentManager.beginTransaction()
    private var idMenu = R.menu.call_menu
    private val REQUEST_CALL = 1
    private val mobile: String = ""
    private var userID = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        makeThisLauncherActivity()
        userID = PreferenceUtils.getID(this).toString()
        //Toast.makeText(applicationContext,userID,Toast.LENGTH_LONG).show();
        if (userID.isEmpty() || userID == "null") {
            fragment = LoginFragment()
            idMenu = R.menu.login_menu
        } else {
            fragment = CallFragment()
            idMenu = R.menu.call_menu
        }
        replaceFragment(fragment)
        /*binding.buttonVisitSite.setOnClickListener{
            startActivity(Intent(this@MainActivity, WebViewActivity::class.java))
        }*/
    }

    private fun makeThisLauncherActivity() {
        if (!isMyLauncherDefault()) {
            val p = packageManager
            val cN = ComponentName(applicationContext, FakeHome::class.java)
            p.setComponentEnabledSetting(
                cN,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
            val selector = Intent(Intent.ACTION_MAIN)
            selector.addCategory(Intent.CATEGORY_HOME)
            startActivity(selector)
            p.setComponentEnabledSetting(
                cN,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentTransaction.replace(R.id.fragment_container_view, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(idMenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.logout_menu_id) {
            PreferenceUtils.saveID("", this@MainActivity)
            PreferenceUtils.saveTime("", this@MainActivity)
            PreferenceUtils.saveBalance("", this@MainActivity)
            Toast.makeText(this@MainActivity, "Logout", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MainActivity, SplashActivity::class.java))
            finish()
        } else if (id == R.id.exit_menu_id) {
            exitTheApp()
            Toast.makeText(this@MainActivity, "Exit", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            } else Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    //TODO: Hi How are you
    private fun makePhoneCall() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity, Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL
            )
        } else {
            val dial = "tel:$mobile"
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
        }
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    private fun exitTheApp() {
        val deleteDialogView: View =
            LayoutInflater.from(this@MainActivity).inflate(R.layout.close_app, null)
        val passwordEditText = deleteDialogView.findViewById<EditText>(R.id.editTextTextPassword)
        val wrongPassword = deleteDialogView.findViewById<TextView>(R.id.textView3)
        wrongPassword.visibility = View.GONE
        val dialogBuilder = AlertDialog.Builder(this@MainActivity)
        dialogBuilder.setView(deleteDialogView)
        dialogBuilder.setTitle("Close the app")
        val okButton = deleteDialogView.findViewById<Button>(R.id.button2)
        val cancelButton = deleteDialogView.findViewById<Button>(R.id.button3)
        val alertDialog = dialogBuilder.create()
        okButton.setOnClickListener {
            val password = passwordEditText.text.toString().trim { it <= ' ' }
            //val adminNum = PreferenceUtils.getAdminNum(this@MainActivity)
            checkAdminPassword(password, wrongPassword, alertDialog)
        }
        cancelButton.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

    private fun checkAdminPassword(
        password: String,
        wrongPassword: TextView,
        alertDialog: AlertDialog
    ) {
        val request: StringRequest =
            object : StringRequest(Method.POST, Constants.CHECK_ADMIN_PASSWORD, object :
                Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    try {
                        val json = response.toString()
                        val dataJson = JSONObject(json)
                        val error = dataJson.getString("error")
                        val message = dataJson.getString("message")
                        if (error.equals("false")) {
                            //val admin = dataJson.getJSONObject("admin")
                            //val adminPassword = admin.getString("password")
                            packageManager.clearPackagePreferredActivities(getPackageName())
                            val intent = Intent(Intent.ACTION_MAIN)
                            intent.addCategory(Intent.CATEGORY_HOME)
                            startActivity(intent)
                            alertDialog.dismiss()
                        } else {
                            wrongPassword.text = message
                            wrongPassword.visibility = View.VISIBLE
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    Toast.makeText(
                        applicationContext,
                        "error!" + volleyError.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["password"] = password
                    return params
                }
            }
        val requestQueue = Volley.newRequestQueue(applicationContext)
        requestQueue.add(request)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK) || (keyCode == KeyEvent.KEYCODE_HOME) || (keyCode == KeyEvent.KEYCODE_MENU)) {
            exitTheApp()
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun isMyLauncherDefault(): Boolean {
        val localPackageManager = packageManager
        val intent = Intent("android.intent.action.MAIN")
        intent.addCategory("android.intent.category.HOME")
        val str = localPackageManager.resolveActivity(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )!!.activityInfo.packageName
        return str == packageName
    }
}