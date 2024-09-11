package com.rspl.rojgaarrakshak.models.SigninResponse

import com.google.gson.annotations.SerializedName

data class SigninResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("otp"     ) var otp     : String?  = null
)
