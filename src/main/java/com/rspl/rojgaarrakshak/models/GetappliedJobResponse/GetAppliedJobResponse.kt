package com.rspl.rojgaarrakshak.models.GetappliedJobResponse

import com.google.gson.annotations.SerializedName

data class GetAppliedJobResponse(
    @SerializedName("status"       ) var status      : Boolean?               = null,
    @SerializedName("message"      ) var message     : String?                = null,
    @SerializedName("applied_jobs" ) var appliedJobs : List<AppliedJobs>?
)
