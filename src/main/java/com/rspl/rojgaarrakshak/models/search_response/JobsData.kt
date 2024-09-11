package com.rspl.rojgaarrakshak.models.search_response

import com.google.gson.annotations.SerializedName

data class JobsData(
    @SerializedName("id"              ) var id             : Int?    = null,
    @SerializedName("skill_id"        ) var skillId        : Int?    = null,
    @SerializedName("job_title"       ) var jobTitle       : String? = null,
    @SerializedName("company_name"    ) var companyName    : String? = null,
    @SerializedName("salary"          ) var salary         : String? = null,
    @SerializedName("job_description" ) var jobDescription : String? = null,
    @SerializedName("experience"      ) var experience     : String? = null,
    @SerializedName("designation"     ) var designation    : String? = null,
    @SerializedName("location"        ) var location       : String? = null,
    @SerializedName("exp_date"        ) var expDate        : String? = null,
    @SerializedName("created_at"      ) var createdAt      : String? = null,
    @SerializedName("updated_at"      ) var updatedAt      : String? = null,
    @SerializedName("skill_title"     ) var skillTitle     : String? = null

)
