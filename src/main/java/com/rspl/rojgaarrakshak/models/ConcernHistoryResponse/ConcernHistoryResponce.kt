package com.rspl.rojgaarrakshak.models.ConcernHistoryResponse

import com.google.gson.annotations.SerializedName

data class ConcernHistoryResponce(
    @SerializedName("status"       ) var status      : Boolean?               = null,
    @SerializedName("message"      ) var message     : String?                = null,
    @SerializedName("concern_data" ) var concernData : ArrayList<ConcernData> = arrayListOf()
)
