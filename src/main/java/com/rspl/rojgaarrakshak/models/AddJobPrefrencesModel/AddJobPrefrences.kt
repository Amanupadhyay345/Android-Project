package com.rspl.rojgaarrakshak.models.AddJobPrefrencesModel

import com.google.gson.annotations.SerializedName

data class AddJobPrefrences(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("user_id" ) var userId  : Int?     = null
)
