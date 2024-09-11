package com.rspl.rojgaarrakshak.models.RequestforAccountDeactivation

import com.google.gson.annotations.SerializedName

data class RequestData(
    @SerializedName("user_id"    ) var userId    : Int?    = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("id"         ) var id        : Int?    = null
)
