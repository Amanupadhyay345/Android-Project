package com.rspl.rojgaarrakshak.landing.verify_otp_aadhar

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.google.gson.annotations.SerializedName
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.models.ResendSignupResponse.ResendSignUpResponse
import com.rspl.rojgaarrakshak.models.VerifyOtpSignup.Signup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class AdharAndOTViewModel  @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var phoneNum = ""
    var source = ""

    private data class Resendotp(@SerializedName("phone_number" ) var phoneNumber : String? = null)

    var getOtpResponse = MutableLiveData<NetworkResult<Signup>>()
    val resendotpResponse = MutableLiveData<NetworkResult<ResendSignUpResponse>>()
    fun verifyOTP(ctx: Context, mobileNumber : String, otp : String,adharNum: String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(getOtpResponse)) {
            getOtpResponse.postValue(NetworkResult.Loading())
            val response = repository.verifyOTP(ReqModel(mobileNumber,otp,adharNum))
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



    private data class ReqModel(var phone_number : String, var otp : String,  var aadhar_number : String)
}