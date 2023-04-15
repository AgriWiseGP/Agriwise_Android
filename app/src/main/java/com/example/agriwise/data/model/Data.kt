package com.example.example

import com.google.gson.annotations.SerializedName


data class Data (

    @SerializedName("id"            ) var id           : Int?    = null,
    @SerializedName("soil_elements" ) var soilElements : Int?    = null,
    @SerializedName("name"          ) var name         : String? = null,
    @SerializedName("created_at"    ) var createdAt    : String? = null,
    @SerializedName("user"          ) var user         : Int?    = null

)