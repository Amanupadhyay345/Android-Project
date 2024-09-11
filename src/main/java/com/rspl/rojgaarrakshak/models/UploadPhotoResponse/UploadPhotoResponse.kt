package com.rspl.rojgaarrakshak.models.UploadPhotoResponse

import com.google.gson.annotations.SerializedName

data class UploadPhotoResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null
)
