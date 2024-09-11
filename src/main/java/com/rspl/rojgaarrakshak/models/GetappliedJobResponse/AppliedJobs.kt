package com.rspl.rojgaarrakshak.models.GetappliedJobResponse

import com.google.gson.annotations.SerializedName

data class AppliedJobs(
    @SerializedName("id"           ) var id          : Int?    = null,
    @SerializedName("role_id"      ) var roleId      : Int?    = null,
    @SerializedName("job_title"    ) var jobTitle    : String? = null,
    @SerializedName("company_name" ) var companyName : String? = null,
    @SerializedName("location"     ) var location    : String? = null,
    @SerializedName("exp_date"     ) var expDate     : String? = null,
    @SerializedName("created_at"   ) var createdAt   : String? = null,
    @SerializedName("updated_at"   ) var updatedAt   : String? = null
)
