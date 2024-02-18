package com.example.pawandroid.service

import com.example.pawandroid.model.PetResponse
import com.example.pawandroid.model.Pets
import com.example.pawandroid.model.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PawService {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<User>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String
    ): Call<User>

    @GET("pets")
    fun getPetsList(): Call<List<Pets>>

    @GET("pets/{id}")
    fun getPet(@Path("id") id: Int) : Call<PetResponse>
}