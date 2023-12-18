package email.phone.signinwithphonedemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import email.phone.signinwithphonedemo.databinding.ActivityAuthBinding

//
// Created by Dev on 20-11-2023.
//
class AuthActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthBinding
    private val PHONE_COUNTRY = "+xx"
    private val PHONE_NUMBER = "xxxxxxxxxx"
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

        val url = "https://auth.phone.email/sign-in?countrycode=$PHONE_COUNTRY" +
                "&phone_no=$PHONE_NUMBER" +
                "&auth_type=1" +
                "&device=$deviceId"

        binding.wvAuth.addJavascriptInterface(WebAppInterface(this),"Android")
        binding.wvAuth.loadUrl(url)
        binding.wvAuth.webViewClient = client
    }

    private val client = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            // Handle the URL loading within the WebView
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
            // Handle the JWT data received from JavaScript
            // Set the result and finish the AuthActivity
            val resultIntent = Intent().apply {
                putExtra("jwt", data)
            }
            activity.setResult(Activity.RESULT_OK, resultIntent)
            activity.finish()
        }
    }


}