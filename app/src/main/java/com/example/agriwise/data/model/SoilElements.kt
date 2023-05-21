package com.example.agriwise.data.model

import com.google.gson.annotations.SerializedName


data class SoilElements (

    @SerializedName("id" ) var id : Int?    = null,
    @SerializedName("n"  ) var n  : Int?    = null,
    @SerializedName("p"  ) var p  : Double? = null,
    @SerializedName("k"  ) var k  : Int?    = null,
    @SerializedName("ph" ) var ph : Double? = null,
    @SerializedName("ec" ) var ec : Double? = null,
    @SerializedName("oc" ) var oc : Double? = null,
    @SerializedName("s"  ) var s  : Double? = null,
    @SerializedName("zn" ) var zn : Double? = null,
    @SerializedName("fe" ) var fe : Double? = null,
    @SerializedName("cu" ) var cu : Double? = null,
    @SerializedName("mn" ) var mn : Double? = null,
    @SerializedName("b"  ) var b  : Double? = null

)