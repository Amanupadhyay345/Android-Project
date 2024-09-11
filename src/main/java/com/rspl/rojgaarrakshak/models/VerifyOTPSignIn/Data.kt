package com.rspl.rojgaarrakshak.models.VerifyOTPSignIn

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("otp_check" ) var otpCheck : Boolean? = null
)
