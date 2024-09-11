package com.rspl.rojgaarrakshak.models.GetUserDetails

import com.google.gson.annotations.SerializedName

data class UserDataResponse(

    @SerializedName("status"                 ) var status              : Boolean?                       = null,
    @SerializedName("message"                ) var message             : String?                        = null,
    @SerializedName("user_data"              ) var userData            : UserData?                      = UserData(),
    @SerializedName("emp_data"               ) var empData             : ArrayList<EmpData>             = arrayListOf(),
    @SerializedName("profile_picture"        ) var profilePicture      : ArrayList<ProfilePicture>      = arrayListOf(),
    @SerializedName("uploaded_docs"          ) var uploadedDocs        : ArrayList<UploadedDocs>        = arrayListOf(),
    @SerializedName("user_prefered_job_data" ) var userPreferedJobData : ArrayList<UserPreferedJobData> = arrayListOf()


)
