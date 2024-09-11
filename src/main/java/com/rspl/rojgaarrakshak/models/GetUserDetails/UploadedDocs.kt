package com.rspl.rojgaarrakshak.models.GetUserDetails

import com.google.gson.annotations.SerializedName

data class UploadedDocs(
    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("user_id"    ) var userId    : Int?    = null,
    @SerializedName("resume"     ) var resume    : String? = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null
)
