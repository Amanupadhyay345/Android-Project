package com.rspl.rojgaarrakshak.models.GetSalarySlipResponse

import com.google.gson.annotations.SerializedName

data class SalarySlipData(
    @SerializedName("month"       ) var month      : String? = null,
    @SerializedName("salary_slip" ) var salarySlip : String? = null
)
