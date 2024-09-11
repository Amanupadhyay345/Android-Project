package com.rspl.rojgaarrakshak.models.PaymentInisialRespose

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("merchantId"            ) var merchantId            : String?             = null,
    @SerializedName("merchantTransactionId" ) var merchantTransactionId : String?             = null,
    @SerializedName("instrumentResponse"    ) var instrumentResponse    : InstrumentResponse? = InstrumentResponse()
)
