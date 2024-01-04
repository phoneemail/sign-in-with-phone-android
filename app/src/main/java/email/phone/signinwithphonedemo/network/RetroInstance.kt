package email.phone.signinwithphonedemo.network

import email.phone.signinwithphonedemo.utils.AppConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by dev on 25/09/2023.
 */
class RetroInstance {

    companion object {

        fun getRetroInstance(): Retrofit {

            val client = OkHttpClient.Builder()
//                .addInterceptor(interceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(client)
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}