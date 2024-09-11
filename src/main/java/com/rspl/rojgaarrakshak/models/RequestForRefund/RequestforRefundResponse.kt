package com.rspl.rojgaarrakshak.models.RequestForRefund

import com.google.gson.annotations.SerializedName

data class RequestforRefundResponse(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null
)
