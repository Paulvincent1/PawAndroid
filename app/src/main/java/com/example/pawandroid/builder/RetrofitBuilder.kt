package com.example.pawandroid.builder

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "http://192.168.100.192/api/"
//                               nath="http://192.168.100.192/api/", paul="http://192.168.0.13/api/"
    private var authToken: String? = null // Variable to hold the authentication token

    // Function to set the authentication token
    fun setAuthToken(token: String) {
        authToken = token
    }

    // Function to build the service instance using the stored authentication token
    fun <T> buildService(serviceType: Class<T>): T {
        val httpClient = OkHttpClient.Builder()

        // Add interceptor for adding authentication token to each request if token is provided
        if (!authToken.isNullOrBlank()) {
            val interceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $authToken")
                    .build()
                chain.proceed(request)
            }
            httpClient.addInterceptor(interceptor)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

        return retrofit.create(serviceType)
    }
}