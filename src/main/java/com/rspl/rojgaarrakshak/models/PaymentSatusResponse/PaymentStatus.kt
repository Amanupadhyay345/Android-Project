package com.rspl.rojgaarrakshak.models.PaymentSatusResponse

import com.google.gson.annotations.SerializedName

data class PaymentStatus(
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("code"    ) var code    : String?  = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : Data?    = Data()
)
