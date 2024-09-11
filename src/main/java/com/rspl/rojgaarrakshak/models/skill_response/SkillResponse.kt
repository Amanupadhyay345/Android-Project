package com.rspl.rojgaarrakshak.models.skill_response


import com.google.gson.annotations.SerializedName

data class SkillResponse(


    @SerializedName("status"      ) var status     : Boolean?              = null,
    @SerializedName("message"     ) var message    : String?               = null,
    @SerializedName("skills_data" ) var skillsData : ArrayList<SkillsData> = arrayListOf()

)