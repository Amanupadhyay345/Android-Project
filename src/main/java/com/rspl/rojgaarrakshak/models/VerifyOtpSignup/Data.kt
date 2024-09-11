package com.rspl.rojgaarrakshak.models.VerifyOtpSignup

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("otp_check" ) var otpCheck : Boolean? = null

)

