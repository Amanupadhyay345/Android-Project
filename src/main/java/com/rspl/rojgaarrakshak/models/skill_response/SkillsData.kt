package com.rspl.rojgaarrakshak.models.skill_response


import com.google.gson.annotations.SerializedName

data class SkillsData(

//    @SerializedName("id")
//    val id: Int?,
//    @SerializedName("skill")
//    val skill: String?,
//    var isChecked : Boolean = false

    @SerializedName("id"          ) var id          : Int?    = null,
    @SerializedName("skill_title" ) var skillTitle  : String? = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("is_status"   ) var isStatus    : Int?    = null,
    @SerializedName("created_at"  ) var createdAt   : String? = null,
    @SerializedName("updated_at"  ) var updatedAt   : String? = null,
    var ischecked:Boolean=false

)