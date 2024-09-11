package com.rspl.rojgaarrakshak.models.ConcernHistoryResponse

import com.google.gson.annotations.SerializedName

data class ConcernData(
    @SerializedName("id"         ) var id        : Int?              = null,
    @SerializedName("user_id"    ) var userId    : Int?              = null,
    @SerializedName("caseId"     ) var caseId    : String?           = null,
    @SerializedName("message"    ) var message   : String?           = null,
    @SerializedName("status"     ) var status    : Int?              = null,
    @SerializedName("created_at" ) var createdAt : String?           = null,
    @SerializedName("updated_at" ) var updatedAt : String?           = null,
    @SerializedName("remarks"    ) var remarks   : ArrayList<Remarks> = arrayListOf()
)
