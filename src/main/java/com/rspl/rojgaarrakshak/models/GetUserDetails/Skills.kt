package com.rspl.rojgaarrakshak.models.GetUserDetails

import com.google.gson.annotations.SerializedName

data class Skills(

    @SerializedName("skill_title" ) var skillTitle : String? = null,
    @SerializedName("id"          ) var id         : Int?    = null



)
