package com.rspl.rojgaarrakshak.models.CityResponce

import com.google.gson.annotations.SerializedName

data class CitiesData(
    @SerializedName("id"   ) var id   : Int?    = null,
    @SerializedName("name" ) var name : String? = null
)
