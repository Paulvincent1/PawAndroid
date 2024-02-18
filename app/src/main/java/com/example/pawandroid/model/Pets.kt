package com.example.pawandroid.model

import com.google.gson.annotations.SerializedName

data class Pets(
    @SerializedName("id")
    val id : Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("age")
    val age: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("breed")
    val breed: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("user")
    val user: User // Include user object within the Pet class
)

data class PetResponse(
    @SerializedName("pet")
    val pet: Pets,
)

