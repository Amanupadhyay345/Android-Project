package com.rspl.rojgaarrakshak.models.CreateConcernResponse

import com.google.gson.annotations.SerializedName

data class ConcernResponce(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : String?  = null
)
