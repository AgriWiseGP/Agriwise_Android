package com.example.agriwise.data.model

import com.google.gson.annotations.SerializedName


data class PlantDiseaseResponse (

    @SerializedName("id"          ) var id         : Int?    = null,
    @SerializedName("disease"     ) var disease    : String? = null,
    @SerializedName("created_at"  ) var createdAt  : String? = null,
    @SerializedName("plant_image" ) var plantImage : Int?    = null,
    @SerializedName("user"        ) var user       : Int?    = null

)