package com.rspl.rojgaarrakshak.models.PaymentInisialRespose

import com.google.gson.annotations.SerializedName

data class PaymentInisialResponse(
    @SerializedName("status"       ) var status      : Boolean?     = null,
    @SerializedName("message"      ) var message     : String?      = null,
    @SerializedName("payment_data" ) var paymentData : PaymentData? = PaymentData()
)
