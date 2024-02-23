package com.example.pawandroid.model

import com.google.gson.annotations.SerializedName

data class Adopt(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("contact_number")
    val contact_number: String,
    @SerializedName("veterinary_information")
    val veterinary_information: Boolean,
    @SerializedName("adoption_agreement")
    val adoption_agreement: Boolean,
    @SerializedName("additional_comment")
    val additional_comment: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("user_id")
    val userid: Int,
    @SerializedName("pet")
    val pet: Pets,
)
