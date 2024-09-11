package com.rspl.rojgaarrakshak.models.GetallyearsResponse

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("emp_id" ) var empId : Int? = null,
    @SerializedName("year"   ) var year  : Int? = null
)
