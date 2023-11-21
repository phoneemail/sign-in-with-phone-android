package email.phone.signinwithphonedemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTParser
import email.phone.signinwithphonedemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPhone.setOnClickListener{
            //Open Webview activity on current screen
            val intent = Intent(this, AuthActivity::class.java)
            launcher.launch(intent)
        }
    }

    // Declare the launcher
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Handle the result data, extract JWT if available
        val jwt = result.data?.getStringExtra("jwt")
        if (jwt != null) {
            decodeJwt(jwt)
        }
    }

    private fun showDetails(jwt:String) {
        binding.tvJwt.text = jwt
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
        showDetails("$phoneCountry $phoneNumber")
        // use this verified phone country and phone number to register this user on your app backend
//        registerUser(phoneCountry, phoneNumber, jwt)
    }
}