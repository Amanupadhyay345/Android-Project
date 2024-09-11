package com.rspl.rojgaarrakshak.models.RequestforAccountDeactivation

import com.google.gson.annotations.SerializedName

data class RequestforAccountDeactivationResponse(
    @SerializedName("status"       ) var status      : Boolean?     = null,
    @SerializedName("message"      ) var message     : String?      = null,
    @SerializedName("request_data" ) var requestData : RequestData? = RequestData()
)
