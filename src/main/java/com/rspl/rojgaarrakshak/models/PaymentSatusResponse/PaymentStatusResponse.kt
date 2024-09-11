package com.rspl.rojgaarrakshak.models.PaymentSatusResponse

import com.google.gson.annotations.SerializedName

data class PaymentStatusResponse(
    @SerializedName("status"         ) var status        : Boolean?       = null,
    @SerializedName("message"        ) var message       : String?        = null,
    @SerializedName("payment_status" ) var paymentStatus : PaymentStatus? = PaymentStatus()

)
