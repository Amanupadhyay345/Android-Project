package com.rspl.rojgaarrakshak.models.VerifyOtpSignup

import com.google.gson.annotations.SerializedName

data class Signup(
    @SerializedName("status"        ) var status        : Boolean?       = null,
    @SerializedName("message"       ) var message       : String?        = null,
    @SerializedName("data"          ) var data          : Data?          = Data(),
    @SerializedName("authorisation" ) var authorisation : Authorisation? = Authorisation()
)
