package com.rspl.rojgaarrakshak.models.Get_All_Job_Response

import com.google.gson.annotations.SerializedName

data class GetjobResponce(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()

)
