package com.rspl.rojgaarrakshak.models.WorkHistoryResponse

import com.google.gson.annotations.SerializedName

data class WorkHistory(
    @SerializedName("id"                ) var id              : Int?    = null,
    @SerializedName("user_id"           ) var userId          : Int?    = null,
    @SerializedName("job_id"            ) var jobId           : Int?    = null,
    @SerializedName("emp_id"            ) var empId           : String? = null,
    @SerializedName("emp_name"          ) var empName         : String? = null,
    @SerializedName("job_location"      ) var jobLocation     : String? = null,
    @SerializedName("sex"               ) var sex             : String? = null,
    @SerializedName("mother_toungue"    ) var motherToungue   : String? = null,
    @SerializedName("marital_status"    ) var maritalStatus   : String? = null,
    @SerializedName("department"        ) var department      : String? = null,
    @SerializedName("designation"       ) var designation     : String? = null,
    @SerializedName("uan"               ) var uan             : String? = null,
    @SerializedName("qualification"     ) var qualification   : String? = null,
    @SerializedName("bank_name"         ) var bankName        : String? = null,
    @SerializedName("bank_ac_no"        ) var bankAcNo        : String? = null,
    @SerializedName("ifsc"              ) var ifsc            : String? = null,
    @SerializedName("doj"               ) var doj             : String? = null,
    @SerializedName("is_job_ended"      ) var isJobEnded      : Int?    = null,
    @SerializedName("end_date"          ) var endDate         : String? = null,
    @SerializedName("created_at"        ) var createdAt       : String? = null,
    @SerializedName("updated_at"        ) var updatedAt       : String? = null,
    @SerializedName("name"              ) var name            : String? = null,
    @SerializedName("dob"               ) var dob             : String? = null,
    @SerializedName("email"             ) var email           : String? = null,
    @SerializedName("phone_number"      ) var phoneNumber     : String? = null,
    @SerializedName("location"          ) var location        : String? = null,
    @SerializedName("skills"            ) var skills          : String? = null,
    @SerializedName("otp"               ) var otp             : String? = null,
    @SerializedName("is_phone_verified" ) var isPhoneVerified : Int?    = null,
    @SerializedName("is_deactivated"    ) var isDeactivated   : Int?    = null,
    @SerializedName("skill_id"          ) var skillId         : Int?    = null,
    @SerializedName("job_title"         ) var jobTitle        : String? = null,
    @SerializedName("company_name"      ) var companyName     : String? = null,
    @SerializedName("salary"            ) var salary          : String? = null,
    @SerializedName("job_description"   ) var jobDescription  : String? = null,
    @SerializedName("experience"        ) var experience      : String? = null,
    @SerializedName("exp_date"          ) var expDate         : String? = null

)
