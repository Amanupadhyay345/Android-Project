package com.rspl.rojgaarrakshak.models.ResendSignupResponse

import com.google.gson.annotations.SerializedName

data class ResendSignUpResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("otp"     ) var otp     : String?  = null
)
