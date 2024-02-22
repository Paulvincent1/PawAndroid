package com.example.pawandroid.model

import com.google.gson.annotations.SerializedName

data class History(
    @SerializedName("status")
    val status: String,
    @SerializedName("pet_name")
    val petname: String,
    @SerializedName("pet_img")
    val petimg: String,
    @SerializedName("details_url")
    val details: String

)
