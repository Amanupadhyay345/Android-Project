package com.rspl.rojgaarrakshak.models.GetSalarySlipResponse

import com.google.gson.annotations.SerializedName

data class GetSalarySlipResponse(
    @SerializedName("status"          ) var status         : Boolean?                  = null,
    @SerializedName("message"         ) var message        : String?                   = null,
    @SerializedName("salarySlip_data" ) var salarySlipData : ArrayList<SalarySlipData> = arrayListOf()
)
