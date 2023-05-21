package com.example.agriwise.data.model

import com.google.gson.annotations.SerializedName


data class SoilQualityResponse (

    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : DataSoilQuality?   = DataSoilQuality()

)