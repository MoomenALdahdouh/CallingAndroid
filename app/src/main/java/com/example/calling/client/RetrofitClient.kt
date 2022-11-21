package com.example.calling.client

import com.example.calling.interfaces.InterfaceApi
import com.example.calling.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val origin = chain.request()
            val requestBuilder = origin.newBuilder()
                .addHeader("Authorizition", Constants.AUTH)
                .method(origin.method(), origin.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

    val instant: InterfaceApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
        retrofit.create(InterfaceApi::class.java)
    }
}