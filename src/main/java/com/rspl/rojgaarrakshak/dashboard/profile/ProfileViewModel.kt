package com.rspl.rojgaarrakshak.dashboard.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.google.gson.annotations.SerializedName
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.landing.signup.SignUpViewModel
import com.rspl.rojgaarrakshak.models.UpdateUserdata.UpdateUserData
import com.rspl.rojgaarrakshak.models.UploadDocResponse.UploadDocResponse
import com.rspl.rojgaarrakshak.models.UploadPhotoResponse.UploadPhotoResponse
import com.rspl.rojgaarrakshak.models.skill_response.SkillResponse
import com.rspl.rojgaarrakshak.models.skill_response.SkillsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {


    var selectedFileUri : Uri? = null
    var selectedFilePath : String = ""
    var UserProfilePicture:String =""

    var GetUserSkillid:String=""
    var fileSize:Double = 0.00

    var selectedFileUriforimage : Uri? = null
    var selectedFilePathforimage : String = ""
    var dobDate : String = ""
    var uploadResponse = MutableLiveData<NetworkResult<UploadDocResponse>>()
    var updateUserResponse = MutableLiveData<NetworkResult<UpdateUserData>>()
    var updateprofileResponse=MutableLiveData<NetworkResult<UploadPhotoResponse>>()

    var skillList = arrayListOf<SkillsData?>()
    var getSkillsResponse : MutableLiveData<NetworkResult<SkillResponse>> = MutableLiveData<NetworkResult<SkillResponse>>()

    init {
        getSkills()
    }




    fun getSkills(){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(getSkillsResponse)) {
            getSkillsResponse.postValue(NetworkResult.Loading())
            val response = repository.getSkills()
            handleResponse(response,getSkillsResponse)
        }
    }

    fun uploadImage(
        ctx : Context,
        userId: Int,
        filePath: String,
        fileUri: Uri
    ) {

        val file = File(filePath)
        val fileBody: RequestBody =
            RequestBody.create(
                ctx.getContentResolver().getType(fileUri).toString()
                    .toMediaTypeOrNull(), file
            )

        val body = MultipartBody.Part.createFormData("resume", file.name, fileBody)

        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(uploadResponse)) {
            uploadResponse.postValue(NetworkResult.Loading())
            val response = repository.uploadDocRequest(userId,body)
            handleResponse(response,uploadResponse)
        }
    }

    fun UpdateUser(token: String,name : String, location : String,gender:String,maritialStatus:String,Mothertongue:String){

        var skillListTemp = arrayListOf<String>()
        var nreplaced:String
        nreplaced=""
        skillList.filterIndexed { index, skillsData ->  skillsData!!.ischecked  }.forEach {
            skillListTemp.add(it!!.id.toString())

            var skillid=skillListTemp.toString()

            var   repleaceid=skillid.replace("[","")
            nreplaced=repleaceid.replace("]","")

        }

        if (nreplaced.isEmpty())
        {
            nreplaced=GetUserSkillid
        }
        var reqModel =
            UpdateRequestModel(nreplaced,location,name,dobDate,gender,maritialStatus,Mothertongue)


        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(updateUserResponse)) {
            updateUserResponse.postValue(NetworkResult.Loading())
            val Response = repository.UpdateUser(token,reqModel)
            handleResponse(Response,updateUserResponse)
        }
    }

    private data class UpdateRequestModel(  @SerializedName("skills"         ) var skills        : String,
                                            @SerializedName("location"       ) var location      : String,
                                            @SerializedName("name"           ) var name          : String,
                                            @SerializedName("dob"            ) var dob           : String,
                                            @SerializedName("gender"         ) var gender        : String,
                                            @SerializedName("marital_status" ) var maritalStatus : String,
                                            @SerializedName("mother_tounge"  ) var motherTounge  : String,
      )


    fun UpdateProfile(
        ctx : Context,
        userId: Int,
        filePath: String,
        fileUri: Uri
    ) {

        val file = File(filePath)
        val fileBody: RequestBody =
            RequestBody.create(
                ctx.getContentResolver().getType(fileUri).toString()
                    .toMediaTypeOrNull(), file
            )

        val body = MultipartBody.Part.createFormData("profile_picture", file.name, fileBody)

        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(updateprofileResponse)) {
            updateprofileResponse.postValue(NetworkResult.Loading())
            val response = repository.uploadProfilepicture(userId,body)
            handleResponse(response,updateprofileResponse)
        }
    }



}