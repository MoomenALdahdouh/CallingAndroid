package com.example.calling.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.calling.databinding.ActivityMainBinding
import com.example.calling.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.webViewClient = WebViewClient()

        // this will load the url of the website
        binding.webView.loadUrl("https://www.chamanvatikagurukul.in/New/index.php")

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        binding.webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        binding.webView.settings.setSupportZoom(true)
    }

    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (binding.webView.canGoBack())
            binding.webView.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }


}