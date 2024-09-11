package com.rspl.rojgaarrakshak.models.ResendSigninResponse

import com.google.gson.annotations.SerializedName

data class ResendsigninResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("otp"     ) var otp     : String?  = null
)
