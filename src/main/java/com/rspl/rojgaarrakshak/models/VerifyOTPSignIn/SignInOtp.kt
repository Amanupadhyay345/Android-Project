package com.rspl.rojgaarrakshak.models.VerifyOTPSignIn

import com.google.gson.annotations.SerializedName

data class SignInOtp(
    @SerializedName("status"        ) var status        : Boolean?       = null,
    @SerializedName("message"       ) var message       : String?        = null,
    @SerializedName("data"          ) var data          : Data?          = Data(),
    @SerializedName("authorisation" ) var authorisation : Authorisation? = Authorisation()
)
