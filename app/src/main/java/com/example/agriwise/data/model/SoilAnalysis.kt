package com.example.agriwise.data.model

import com.google.gson.annotations.SerializedName


data class SoilAnalysis (

    @SerializedName("Pratio" ) var Pratio : Double?    = null,
    @SerializedName("Kratio" ) var Kratio : Double? = null,
    @SerializedName("Nratio" ) var Nratio : Double?    = null,
    @SerializedName("PH"     ) var PH     : Double? = null

)