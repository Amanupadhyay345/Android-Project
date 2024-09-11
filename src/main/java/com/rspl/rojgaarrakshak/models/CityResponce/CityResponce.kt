package com.rspl.rojgaarrakshak.models.CityResponce

import com.google.gson.annotations.SerializedName

data class CityResponce(
    @SerializedName("status"      ) var status     : Boolean?              = null,
    @SerializedName("message"     ) var message    : String?               = null,
    @SerializedName("cities_data" ) var citiesData : ArrayList<CitiesData> = arrayListOf()

)
