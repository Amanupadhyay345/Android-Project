package com.rspl.rojgaarrakshak.landing.signup

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.google.gson.annotations.SerializedName
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.landing.verify_otp_aadhar.AdharAndOTViewModel
import com.rspl.rojgaarrakshak.models.ResendSignupResponse.ResendSignUpResponse
import com.rspl.rojgaarrakshak.models.SignUpResponce.signupresponce
import com.rspl.rojgaarrakshak.models.UploadDocResponse.UploadDocResponse
import com.rspl.rojgaarrakshak.models.VerifyOtpSignup.Signup
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
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    var email:String=""

    private data class Resendotp(@SerializedName("phone_number" ) var phoneNumber : String? = null)

    var getOtpResponse = MutableLiveData<NetworkResult<Signup>>()
    val resendotpResponse = MutableLiveData<NetworkResult<ResendSignUpResponse>>()

    var selectedFileUri : Uri? = null
    var selectedFilePath : String = ""

    var fileSize:Double = 0.00
    var skillList = arrayListOf<SkillsData?>()
    var getSkillsResponse : MutableLiveData<NetworkResult<SkillResponse>> = MutableLiveData<NetworkResult<SkillResponse>>()
    var dobDate : String = ""


    var uploadResponse = MutableLiveData<NetworkResult<UploadDocResponse>>()

    var signUpResponse = MutableLiveData<NetworkResult<signupresponce>>()


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

    fun signUpRequest(name : String, email : String, location : String, phone_number : String,gender: String,maritalStatus: String,motherTounge: String){
        var skillListTemp = arrayListOf<String>()
        var nreplaced:String
        nreplaced=""
        skillList.filterIndexed { index, skillsData ->  skillsData!!.ischecked  }.forEach {
            skillListTemp.add(it!!.id.toString())

            val skillid=skillListTemp.toString()
         val repleaceid=skillid.replace("[","")
             nreplaced=repleaceid.replace("]","")


        }

        var reqModel = ReqModel(name,email,phone_number,dobDate,location,nreplaced,gender,maritalStatus,motherTounge )
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(signUpResponse)) {
            signUpResponse.postValue(NetworkResult.Loading())
            val Response = repository.signUpRequest(reqModel)
            handleResponse(Response,signUpResponse)
        }
    }

    private data class ReqModel(@SerializedName("name"           ) var name          : String,
                                @SerializedName("email"          ) var email         : String,
                                @SerializedName("phone_number"   ) var phoneNumber   : String,
                                @SerializedName("dob"            ) var dob           : String,
                                @SerializedName("location"       ) var location      : String,
                                @SerializedName("skills"         ) var skills        : String,
                                @SerializedName("gender"         ) var gender        : String,
                                @SerializedName("marital_status" ) var maritalStatus : String,
                                @SerializedName("mother_tounge"  ) var motherTounge  : String )


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



    fun verifyOTP(ctx: Context, mobileNumber : String, otp : String,adharNum: String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(getOtpResponse)) {
            getOtpResponse.postValue(NetworkResult.Loading())
            val response = repository.verifyOTP(VerifyModel(mobileNumber,otp,adharNum))
            handleResponse(response,getOtpResponse)
        }

    }

    fun SignupResendOtp(mobileNumber : String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(resendotpResponse)) {
            resendotpResponse.postValue(NetworkResult.Loading())
            val response = repository.SignupResendOtp(Resendotp(mobileNumber))
            handleResponse(response,resendotpResponse)
        }

    }



    private data class VerifyModel(var phone_number : String, var otp : String,  var aadhar_number : String)

}