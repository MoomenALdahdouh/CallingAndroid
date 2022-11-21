package com.example.calling.interfaces

import com.example.calling.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface InterfaceApi {
    @FormUrlEncoded
    @POST("user")
    fun userLogin(
        @Field("userID") userID: String,
        @Field("password") password: String,
    ): Call<LoginResponse>
}