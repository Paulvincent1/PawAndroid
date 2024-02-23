package com.example.pawandroid.service

import com.example.pawandroid.model.Adopt
import com.example.pawandroid.model.History
import com.example.pawandroid.model.PetResponse
import com.example.pawandroid.model.Pets
import com.example.pawandroid.model.User
import com.example.pawandroid.model.ViewProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PawService {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<User>
    @Multipart
    @POST("post-for-adoption")
    fun postForAdoption(
        @Part("name") name: RequestBody,
        @Part("age") age: Int,
        @Part("species") species: RequestBody,
        @Part("breed") breed: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("region") region: RequestBody,
        @Part("description") description: RequestBody,
        @Part img: MultipartBody.Part
    ): Call<Pets>

    @Multipart
    @POST("pets/{id}")
    fun updatePet(
        @Path("id") id : Int,
        @Query("_method") method: String,
        @Part("name") name: RequestBody,
        @Part("age") age: Int,
        @Part("species") species: RequestBody,
        @Part("breed") breed: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("region") region: RequestBody,
        @Part("description") description: RequestBody,
        @Part img: MultipartBody.Part
    ): Call<Pets>

    @FormUrlEncoded
    @POST("pets/{id}")
    fun reportPost(
        @Path("id") id: Int,
        @Field("reason") reason: String
    ) : Call<Unit>

    @DELETE("pets/{id}")
    fun deletePet(@Path("id") id :Int): Call<Unit>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String
    ): Call<User>

    @GET("users/current")
    fun getCurrentUser() : Call<User>
    @Multipart
    @POST("users/{id}")
    fun updateUserProfile(
        @Path("id") id: Int,
        @Query("_method") method: String,
        @Part img: MultipartBody.Part
        ) : Call<User>
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


    @GET("pending-request")
    fun getYourRequestList() : Call<List<Adopt>>
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

    @GET("incoming-requests")
    fun getIncomingRequestList() : Call<List<Adopt>>

    @GET("incoming-requests/details/{id}")
    fun getIncomingRequest(@Path("id") id : Int) : Call<Adopt>


    @PUT("incoming-requests/{id}/accept")
    fun acceptIncoming(@Path("id") id: Int) : Call<Unit>


    @PUT("incoming-requests/{id}/reject")
    fun rejectIncoming(@Path("id") id: Int) : Call<Unit>

    @GET("on-going-requests")
    fun onGoingRequestList() : Call<List<Adopt>>

    @GET("on-going-requests/details/{id}")
    fun onGoingRequestDetail(@Path("id") id: Int): Call<Adopt>

    @PUT("on-going-requests/{id}/done")
    fun doneRequest(@Path("id")id : Int): Call<Unit>

    @GET("history")
    fun history(): Call<List<History>>


}