package com.rspl.rojgaarrakshak.models.search_response

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("status"        ) var status       : Boolean?            = null,
    @SerializedName("message"       ) var message      : String?             = null,
    @SerializedName("jobs_data"     ) var jobsData     : ArrayList<JobsData> = arrayListOf(),
    @SerializedName("results_count" ) var resultsCount : Int?                = null
)