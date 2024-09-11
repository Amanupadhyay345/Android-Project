package com.rspl.rojgaarrakshak.models.GetSubSkillModel

import com.google.gson.annotations.SerializedName

data class SubSkillResponse(
    @SerializedName("status"                     ) var status                  : Boolean?                           = null,
    @SerializedName("message"                    ) var message                 : String?                            = null,
    @SerializedName("preference_sub_skills_data" ) var preferenceSubSkillsData : ArrayList<PreferenceSubSkillsData> = arrayListOf()
)
