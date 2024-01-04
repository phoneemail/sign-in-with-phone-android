package email.phone.signinwithphonedemo.network


import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

//
// Created by Dev on 25-09-2023.
//
interface ApiInterface {
    object RetrofitHelper {
        val apiService: ApiInterface
            get() = RetroInstance.getRetroInstance().create(ApiInterface::class.java)
    }

    @FormUrlEncoded
    @POST("email-count")
    fun getEmailCount(@Field("merchant_phone_email_jwt") jwt:String, @Field("source") source:String): Call<JsonObject>
}