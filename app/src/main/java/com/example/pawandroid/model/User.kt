package com.example.pawandroid.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("password_confirmation")
    val password_confirmation: String,
    @SerializedName("token")
    val token: String,
)


