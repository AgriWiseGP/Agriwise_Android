package com.example.agriwise.data.model

import com.google.gson.annotations.SerializedName


data class User (

    @SerializedName("id"                        ) var id                      : String? = null,
    @SerializedName("username"                  ) var username                : String? = null,
    @SerializedName("is_agriculture_specialist" ) var isAgricultureSpecialist : String? = null,
    @SerializedName("date_of_birth " )            var date_of_birth           : String? = null,
    @SerializedName("phone_number" )              var phone_number            : String? = null,
    @SerializedName("email" )                     var email                   : String? = null,
    @SerializedName("image"                     ) var image                   : String? = null
)