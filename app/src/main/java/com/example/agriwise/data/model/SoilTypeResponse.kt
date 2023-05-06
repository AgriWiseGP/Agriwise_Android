package com.example.agriwise.data.model


import com.google.gson.annotations.SerializedName


data class SoilTypeResponse (

    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("soil_type"  ) var soilType  : String? = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("soil_image" ) var soilImage : Int?    = null,
    @SerializedName("user"       ) var user      : Int?    = null

)