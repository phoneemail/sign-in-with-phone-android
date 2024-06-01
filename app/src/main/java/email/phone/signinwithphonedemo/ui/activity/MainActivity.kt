package email.phone.signinwithphonedemo.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import email.phone.signinwithphonedemo.R
import email.phone.signinwithphonedemo.databinding.ActivityMainBinding
import email.phone.signinwithphonedemo.network.ApiInterface
import email.phone.signinwithphonedemo.utils.AppConstants
import email.phone.signinwithphonedemo.utils.click
import email.phone.signinwithphonedemo.utils.gone
import email.phone.signinwithphonedemo.utils.isConnected
import email.phone.signinwithphonedemo.utils.toast
import email.phone.signinwithphonedemo.utils.visible
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showDetails("",1)

        binding.btnPhone.setOnClickListener{
            if(isLoggedIn.not()) {
                if(this.isConnected) {
                    //Open Webview activity on current screen
                    val intent = Intent(this, AuthActivity::class.java)
                    launcher.launch(intent)
                }else{
                    toast { getString(R.string.no_internet) }
                }

            }else{
                isLoggedIn = false
                showDetails("",1)
            }
        }

        binding.rlEmailCount.click {
            if(this.isConnected) {
                //Open Webview activity on current screen
                val intent = Intent(this, WebMailActivity::class.java)
                launcher.launch(intent)
            }else{
                toast { getString(R.string.no_internet) }
            }

        }
    }

    // Declare the launcher
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Handle the result data, extract access_token if available
        val accessToken = result.data?.getStringExtra("access_token")
        if (accessToken != null) {
            if(this.isConnected) {
                //get user info from access token
                getUserInfo(accessToken)
            }else{
                toast { getString(R.string.no_internet) }
            }

        }
    }

    private fun getUserInfo(accessToken: String) {

        val call = ApiInterface.RetrofitHelper.apiService.getUserInfo(accessToken,  AppConstants.CLIENT_ID)
        call.enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.d("TAG", "onResponse: successful ")
                    val userInfo = response.body()!!
                    if(userInfo.isJsonObject && userInfo.get("status").asInt == 200){
                        Log.d("TAG", "onResponse: User info fetched $userInfo")
                        val phoneCountryCode = userInfo.get("country_code").asString
                        val phoneNumber = userInfo.get("phone_no").asString
                        val phJWT = userInfo.get("ph_email_jwt").asString

                        showDetails("$phoneCountryCode$phoneNumber",2)
                        if(this@MainActivity.isConnected) {
                            //Retrieve the email count
                            getEmailCount(phJWT)
                        }else{
                            toast { getString(R.string.no_internet) }
                        }

                    }else{
                        Log.w("TAG", "onResponse: Error in retrieving user info", )
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w("TAG", "onFailure: ${t.message}")
            }
        })
    }

    private fun getEmailCount(jwt: String) {

        val call = ApiInterface.RetrofitHelper.apiService.getEmailCount(jwt, "app")
        call.enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) { 
                    val emailCount = response.body()!!.get("email_count").asString
                    if(emailCount.isNotEmpty()){
                        binding.tvCount.text = emailCount
                        binding.tvCount.visible()
                    }else{
                        binding.tvCount.gone()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.w("TAG", "onFailure: ${t.message}")
            }
        })
    }

    private fun showDetails(verifiedPhoneDetail:String, source:Int) {
        if(source ==1 ){
            binding.tvInfo.text = getString(R.string.sign_in_text)
            binding.tvJwt.text = getString(R.string.sign_in_description)
            binding.btnPhone.text =  getString(R.string.sign_in_with_phone)
//            binding.btnPhone.setBackgroundColor(resources.getColor(R.color.card_background))
            binding.btnPhone.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_call,0,0,0)
            binding.rlEmailCount.gone()
        }else{
            isLoggedIn = true
            binding.tvInfo.text = getString(R.string.you_are_logged_in_with)
            binding.tvJwt.text = getString(R.string.phone_details, verifiedPhoneDetail)
            binding.btnPhone.text =  getString(R.string.logout)
            binding.btnPhone.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0)
            binding.rlEmailCount.visible()
        }
    }

}