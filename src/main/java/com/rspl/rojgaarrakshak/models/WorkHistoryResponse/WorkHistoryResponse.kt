package com.rspl.rojgaarrakshak.models.WorkHistoryResponse

import com.google.gson.annotations.SerializedName

data class WorkHistoryResponse(
    @SerializedName("status"       ) var status      : Boolean?               = null,
    @SerializedName("message"      ) var message     : String?                = null,
    @SerializedName("present_work" ) var presentWork : ArrayList<PresentWork> = arrayListOf(),
    @SerializedName("work_history" ) var workHistory : ArrayList<WorkHistory> = arrayListOf()


)
