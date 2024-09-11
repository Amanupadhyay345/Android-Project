package com.rspl.rojgaarrakshak.models.GetSubSkillModel

import com.google.gson.annotations.SerializedName

data class PreferenceSubSkillsData(
    @SerializedName("id"    ) var id    : Int?    = null,
    @SerializedName("title" ) var title : String? = null

)
