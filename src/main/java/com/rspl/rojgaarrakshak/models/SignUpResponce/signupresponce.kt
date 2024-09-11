package com.rspl.rojgaarrakshak.models.SignUpResponce

import com.google.gson.annotations.SerializedName

data class signupresponce(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : Any,
    @SerializedName("user_id" ) var userId  : Int?     = null
)
