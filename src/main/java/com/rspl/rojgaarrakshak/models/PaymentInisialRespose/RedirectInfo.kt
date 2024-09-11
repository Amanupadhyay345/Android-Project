package com.rspl.rojgaarrakshak.models.PaymentInisialRespose

import com.google.gson.annotations.SerializedName

data class RedirectInfo(
    @SerializedName("url"    ) var url    : String? = null,
    @SerializedName("method" ) var method : String? = null
)
