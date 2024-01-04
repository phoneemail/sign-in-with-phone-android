package email.phone.signinwithphonedemo.ui.activity

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import email.phone.signinwithphonedemo.databinding.ActivityWebmailBinding


//
// Created by Dev on 01-01-2024.
//
class WebMailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityWebmailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.wvMail.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
        }

        binding.wvMail.loadUrl("https://web.phone.email/")
        binding.wvMail.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                // Handle the URL loading within the WebView
                Log.d("client", "shouldOverrideUrlLoading: URL: $url")
                if (url != null) {
                    view.loadUrl(url)
                }
                return false
            }
        }
    }


}