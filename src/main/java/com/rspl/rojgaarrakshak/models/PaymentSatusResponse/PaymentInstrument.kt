package com.rspl.rojgaarrakshak.models.PaymentSatusResponse

import com.google.gson.annotations.SerializedName

data class PaymentInstrument(
    @SerializedName("type"                ) var type                : String? = null,
    @SerializedName("cardType"            ) var cardType            : String? = null,
    @SerializedName("pgTransactionId"     ) var pgTransactionId     : String? = null,
    @SerializedName("bankTransactionId"   ) var bankTransactionId   : String? = null,
    @SerializedName("pgAuthorizationCode" ) var pgAuthorizationCode : String? = null,
    @SerializedName("arn"                 ) var arn                 : String? = null,
    @SerializedName("bankId"              ) var bankId              : String? = null,
    @SerializedName("brn"                 ) var brn                 : String? = null

)
