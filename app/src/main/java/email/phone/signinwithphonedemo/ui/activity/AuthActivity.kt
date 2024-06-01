package email.phone.signinwithphonedemo.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import email.phone.signinwithphonedemo.databinding.ActivityAuthBinding
import email.phone.signinwithphonedemo.utils.AppConstants

//
// Created by Dev on 20-11-2023.
//
class AuthActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthBinding
    private var deviceId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.wvAuth.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
        }

        deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        val url = "https://auth.phone.email/log-in?client_id=${AppConstants.CLIENT_ID}" +
                "&auth_type=1" +
                "&device=$deviceId"

        binding.wvAuth.addJavascriptInterface(WebAppInterface(this),"Android")
        binding.wvAuth.loadUrl(url)
        binding.wvAuth.webViewClient = client
    }

    private val client = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            // Handle the URL loading within the WebView
            Log.d("client", "shouldOverrideUrlLoading: URL: $url")
            if (url != null) {
                view.loadUrl(url)
            }
            return false
        }
    }

    // Define the JavaScript interface
    class WebAppInterface(private val activity: Activity) {
        @JavascriptInterface
        fun sendTokenToApp(data: String) {
            Log.d("WebAppInterface", "sendTokenToApp: ")
            // Handle the access token received from JavaScript
            // Set the result and finish the AuthActivity
            val resultIntent = Intent().apply {
                putExtra("access_token", data)
            }
            activity.setResult(Activity.RESULT_OK, resultIntent)
            activity.finish()
        }
    }
}