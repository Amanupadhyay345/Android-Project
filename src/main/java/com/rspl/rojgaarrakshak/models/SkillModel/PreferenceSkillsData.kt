package com.rspl.rojgaarrakshak.models.SkillModel

import com.google.gson.annotations.SerializedName
import com.rspl.rojgaarrakshak.models.GetSubSkillModel.PreferenceSubSkillsData

data class PreferenceSkillsData(

    @SerializedName("id"              ) var id            : Int?    = null,
    @SerializedName("pre_skill_title" ) var preSkillTitle : String? = null,
    var isExpandable:Boolean=false,

    var preferenceSubSkillsData : ArrayList<PreferenceSubSkillsData> = arrayListOf()
)

