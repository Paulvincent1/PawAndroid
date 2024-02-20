package com.example.pawandroid.service

import com.example.pawandroid.model.Adopt
import com.example.pawandroid.model.PetResponse
import com.example.pawandroid.model.Pets
import com.example.pawandroid.model.User
import com.example.pawandroid.model.ViewProfile
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("users/{id}")
    fun getUser(@Path("id") id: Int) : Call<ViewProfile>
    @GET("pets/user/{id}")
    fun getUserPets(@Path("id") id: Int): Call<List<Pets>>
    @GET("pets")
    fun getPetsList(): Call<List<Pets>>

    @GET("pets/{id}")
    fun getPet(@Path("id") id: Int) : Call<PetResponse>


    //adopt pet
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

    //My request
    @GET("my-requests")
    fun getMyRequestList() : Call<List<Adopt>>

    @GET("my-requests/{id}/edit")
    fun getMyRequest(@Path("id") id: Int) : Call<Adopt>

    @FormUrlEncoded
    @PUT("my-requests/{id}")
    fun updateAdoptionRequest(
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("city") city: String,
        @Field("contact_number") contact_number: String,
        @Field("veterinary_information") veterinary_information: String,
        @Field("adoption_agreement") adoption_agreement: String,
        @Field("additional_comment") additional_comment: String
    ): Call<Adopt>

    @DELETE("my-requests/{id}")
    fun deleteRequest(
        @Path("id") id: Int
    ): Call<Unit>

}