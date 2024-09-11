package com.rspl.rojgaarrakshak.models.SkillModel

import com.google.gson.annotations.SerializedName

data class UserSkillResponse(
    @SerializedName("status"                 ) var status               : Boolean?                        = null,
    @SerializedName("message"                ) var message              : String?                         = null,
    @SerializedName("preference_skills_data" ) var preferenceSkillsData : ArrayList<PreferenceSkillsData> = arrayListOf()

)
