package com.example.pawandroid.model

import com.google.gson.annotations.SerializedName

data class PetSocial(
    @SerializedName("id")
    val id: Int,
    @SerializedName("caption")
    val caption: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("like_count")
    var like_count: Int,
    @SerializedName("is_Liked")
    var is_Liked: Int,
    @SerializedName("user")
    val user: User
)
