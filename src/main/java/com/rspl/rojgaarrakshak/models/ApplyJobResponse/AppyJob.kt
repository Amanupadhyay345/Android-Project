package com.rspl.rojgaarrakshak.models.ApplyJobResponse

import com.google.gson.annotations.SerializedName

data class AppyJob(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : String?  = null
)
