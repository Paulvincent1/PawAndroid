package com.example.pawandroid.service

import com.example.pawandroid.model.Adopt
import com.example.pawandroid.model.PetResponse
import com.example.pawandroid.model.Pets
import com.example.pawandroid.model.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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


    @FormUrlEncoded
    @POST("pets/{id}/adopt")
    fun adoptPet(
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("city") city: String,
        @Field("contact_number") contact_number: String,
        @Field("veterinary_information") veterinary_information: String,
        @Field("adoption_agreement") adoption_agreement: String,
        @Field("additional_comment") additional_comment: String
    ): Call<Adopt>

}