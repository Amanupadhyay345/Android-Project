package com.rspl.rojgaarrakshak.models.ConcernHistoryResponse

import com.google.gson.annotations.SerializedName

data class Remarks(
    @SerializedName("id"           ) var id          : Int?    = null,
    @SerializedName("concern_id"   ) var concernId   : Int?    = null,
    @SerializedName("admin_remark" ) var adminRemark : String? = null,
    @SerializedName("created_at"   ) var createdAt   : String? = null,
    @SerializedName("updated_at"   ) var updatedAt   : String? = null
)
