package com.rspl.rojgaarrakshak.models.GetallyearsResponse

import com.google.gson.annotations.SerializedName

data class GETallyearsResponse(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()

)
