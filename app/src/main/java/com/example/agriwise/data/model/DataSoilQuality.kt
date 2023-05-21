package com.example.agriwise.data.model

import com.google.gson.annotations.SerializedName


data class DataSoilQuality (

    @SerializedName("id"            ) var id           : Int?          = null,
    @SerializedName("quality"       ) var quality      : String?       = null,
    @SerializedName("user"          ) var user         : Int?          = null,
    @SerializedName("created_at"    ) var createdAt    : String?       = null,
    @SerializedName("soil_elements" ) var soilElements : SoilElements? = SoilElements()

)