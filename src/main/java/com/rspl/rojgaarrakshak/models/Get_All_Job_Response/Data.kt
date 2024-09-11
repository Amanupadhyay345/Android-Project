package com.rspl.rojgaarrakshak.models.Get_All_Job_Response

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("job_title" ) var jobTitle : String? = null,
    @SerializedName("id"        ) var id       : Int?    = null
)
