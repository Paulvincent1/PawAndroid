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
    val img: String
)
