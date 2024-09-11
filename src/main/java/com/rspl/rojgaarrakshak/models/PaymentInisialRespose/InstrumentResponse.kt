package com.rspl.rojgaarrakshak.models.PaymentInisialRespose

import com.google.gson.annotations.SerializedName

data class InstrumentResponse(
    @SerializedName("type"         ) var type         : String?       = null,
    @SerializedName("redirectInfo" ) var redirectInfo : RedirectInfo? = RedirectInfo()
)
