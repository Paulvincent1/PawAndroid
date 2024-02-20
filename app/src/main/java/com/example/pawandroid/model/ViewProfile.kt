package com.example.pawandroid.model

import com.google.gson.annotations.SerializedName

data class ViewProfile( @SerializedName("user")
                        val user: User,
                        @SerializedName("pets")
                        val pets: List<Pets>)

