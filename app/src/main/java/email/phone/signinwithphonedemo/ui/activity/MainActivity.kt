package email.phone.signinwithphonedemo.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTParser
import email.phone.signinwithphonedemo.R
import email.phone.signinwithphonedemo.databinding.ActivityMainBinding
import email.phone.signinwithphonedemo.network.ApiInterface
import email.phone.signinwithphonedemo.utils.click
import email.phone.signinwithphonedemo.utils.gone
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
                //Open Webview activity on current screen
                val intent = Intent(this, AuthActivity::class.java)
                launcher.launch(intent)
            }else{
                isLoggedIn = false
                showDetails("",1)
            }
        }

        binding.rlEmailCount.click {
            val intent = Intent(this, WebMailActivity::class.java)
            launcher.launch(intent)
        }
    }

    // Declare the launcher
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Handle the result data, extract JWT if available
        val jwt = result.data?.getStringExtra("jwt")
        if (jwt != null) {
            decodeJwt(jwt)

            // Get email count
            getEmailCount(jwt)
        }
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

    private fun decodeJwt(jwtString: String){
        // Parse the JWT
        val jwt: JWT = JWTParser.parse(jwtString)
        // Get the claims (payload) from the JWT
        val claims = jwt.jwtClaimsSet
        // Now you can access specific claim values
        val phoneCountry: String? = claims.getStringClaim("country_code")
        val phoneNumber: String? = claims.getStringClaim("phone_no")
        // Further authentication logic or UI updates can be performed here
        showDetails("$phoneCountry $phoneNumber", 2)
        // use this verified phone country and phone number to register this user on your app backend
//        registerUser(phoneCountry, phoneNumber, jwt)

    }
}