package com.rspl.rojgaarrakshak.models.GetUserDetails

import com.google.gson.annotations.SerializedName

data class EmpData(

    @SerializedName("id"                    ) var id                  : Int?    = null,
    @SerializedName("user_id"               ) var userId              : Int?    = null,
    @SerializedName("emp_id"                ) var empId               : String? = null,
    @SerializedName("emp_name"              ) var empName             : String? = null,
    @SerializedName("emp_father_name"       ) var empFatherName       : String? = null,
    @SerializedName("emp_mother_name"       ) var empMotherName       : String? = null,
    @SerializedName("sex"                   ) var sex                 : String? = null,
    @SerializedName("annual_salary"         ) var annualSalary        : String? = null,
    @SerializedName("mother_toungue"        ) var motherToungue       : String? = null,
    @SerializedName("marital_status"        ) var maritalStatus       : String? = null,
    @SerializedName("designation"           ) var designation         : String? = null,
    @SerializedName("emp_department"        ) var empDepartment       : String? = null,
    @SerializedName("uan"                   ) var uan                 : String? = null,
    @SerializedName("pancard_number"        ) var pancardNumber       : String? = null,
    @SerializedName("client_id"             ) var clientId            : String? = null,
    @SerializedName("emp_branch"            ) var empBranch           : String? = null,
    @SerializedName("emp_division"          ) var empDivision         : String? = null,
    @SerializedName("bank_name"             ) var bankName            : String? = null,
    @SerializedName("bank_ac_no"            ) var bankAcNo            : String? = null,
    @SerializedName("emp_pf_applicable"     ) var empPfApplicable     : String? = null,
    @SerializedName("emp_pt_applicable"     ) var empPtApplicable     : String? = null,
    @SerializedName("emp_esi_applicable"    ) var empEsiApplicable    : String? = null,
    @SerializedName("emp_pf_no"             ) var empPfNo             : String? = null,
    @SerializedName("emp_esi_number"        ) var empEsiNumber        : String? = null,
    @SerializedName("emp_type"              ) var empType             : String? = null,
    @SerializedName("ifsc"                  ) var ifsc                : String? = null,
    @SerializedName("old_empid"             ) var oldEmpid            : String? = null,
    @SerializedName("doj"                   ) var doj                 : String? = null,
    @SerializedName("is_job_ended"          ) var isJobEnded          : Int?    = null,
    @SerializedName("end_date"              ) var endDate             : String? = null,
    @SerializedName("created_at"            ) var createdAt           : String? = null,
    @SerializedName("updated_at"            ) var updatedAt           : String? = null,
    @SerializedName("emp_permanent_address" ) var empPermanentAddress : String? = null,
    @SerializedName("emp_present_address"   ) var empPresentAddress   : String? = null,
    @SerializedName("is_imported_employee"  ) var isImportedEmployee  : Int?    = null

)
