package com.rspl.rojgaarrakshak.models.GetUserDetails

import com.google.gson.annotations.SerializedName

data class UserData(

    @SerializedName("id"                ) var id              : Int?              = null,
    @SerializedName("name"              ) var name            : String?           = null,
    @SerializedName("dob"               ) var dob             : String?           = null,
    @SerializedName("email"             ) var email           : String?           = null,
    @SerializedName("phone_number"      ) var phoneNumber     : String?           = null,
    @SerializedName("location"          ) var location        : String?           = null,
    @SerializedName("skills"            ) var skills          : ArrayList<Skills> = arrayListOf(),
    @SerializedName("gender"            ) var gender          : String?           = null,
    @SerializedName("marital_status"    ) var maritalStatus   : String?           = null,
    @SerializedName("mother_tounge"     ) var motherTounge    : String?           = null,
    @SerializedName("otp"               ) var otp             : String?           = null,
    @SerializedName("is_phone_verified" ) var isPhoneVerified : Int?              = null,
    @SerializedName("is_deactivated"    ) var isDeactivated   : Int?              = null,
    @SerializedName("payment_status"    ) var paymentStatus   : Int?              = null,
    @SerializedName("is_refund"         ) var isRefund        : Int?              = null,
    @SerializedName("created_at"        ) var createdAt       : String?           = null,
    @SerializedName("updated_at"        ) var updatedAt       : String?           = null


)
