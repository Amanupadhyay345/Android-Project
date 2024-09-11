package com.rspl.rojgaarrakshak.models.GetUserDetails

import com.google.gson.annotations.SerializedName

data class UserPreferedJobData(

    @SerializedName("id"             ) var id           : Int?    = null,
    @SerializedName("user_id"        ) var userId       : Int?    = null,
    @SerializedName("pre_skills"     ) var preSkills    : String? = null,
    @SerializedName("pre_sub_skills" ) var preSubSkills : String? = null,
    @SerializedName("location"       ) var location     : String? = null,
    @SerializedName("state_id"       ) var stateId      : String? = null,
    @SerializedName("created_at"     ) var createdAt    : String? = null,
    @SerializedName("updated_at"     ) var updatedAt    : String? = null

)
