package com.rspl.rojgaarrakshak.models.PaymentSatusResponse

import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("merchantId"            ) var merchantId            : String?            = null,
    @SerializedName("merchantTransactionId" ) var merchantTransactionId : String?            = null,
    @SerializedName("transactionId"         ) var transactionId         : String?            = null,
    @SerializedName("amount"                ) var amount                : Int?               = null,
    @SerializedName("state"                 ) var state                 : String?            = null,
    @SerializedName("responseCode"          ) var responseCode          : String?            = null,
    @SerializedName("paymentInstrument"     ) var paymentInstrument     : PaymentInstrument? = PaymentInstrument()


)
