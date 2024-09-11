package com.rspl.rojgaarrakshak.models.VerifyOtpSignup

import com.google.gson.annotations.SerializedName

data class Authorisation(
    @SerializedName("token" ) var token : String? = null,
    @SerializedName("type"  ) var type  : String? = null
)
