package com.example.agriwise.data.model

import com.google.gson.annotations.SerializedName


data class SoilFertilizerResponse (

    @SerializedName("id"                 ) var id                : Int?               = null,
    @SerializedName("crop_name"          ) var cropName          : String?            = null,
    @SerializedName("soil_name"          ) var soilName          : String?            = null,
    @SerializedName("target"             ) var target            : String?            = null,
    @SerializedName("created_at"         ) var createdAt         : String?            = null,
    @SerializedName("user"               ) var user              : Int?               = null,
    @SerializedName("soil_analysis"      ) var soilAnalysis      : SoilAnalysis?      = SoilAnalysis(),
    @SerializedName("weather_conditions" ) var weatherConditions : WeatherConditions? = WeatherConditions()

)