package com.rspl.rojgaarrakshak.models.UploadDocResponse

import com.google.gson.annotations.SerializedName

data class UploadDocResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null

)
