package com.rspl.rojgaarrakshak.models.GetUserDetails

import com.google.gson.annotations.SerializedName

data class ProfilePicture(

    @SerializedName("id"             ) var id             : Int?    = null,
    @SerializedName("user_id"        ) var userId         : Int?    = null,
    @SerializedName("profilepicture" ) var profilepicture : String? = null,
    @SerializedName("created_at"     ) var createdAt      : String? = null,
    @SerializedName("updated_at"     ) var updatedAt      : String? = null
)
