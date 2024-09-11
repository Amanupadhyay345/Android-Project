package com.rspl.rojgaarrakshak.models.UpdateUserdata

import com.google.gson.annotations.SerializedName

data class UpdateUserData(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("user_id" ) var userId  : Int?     = null

)
