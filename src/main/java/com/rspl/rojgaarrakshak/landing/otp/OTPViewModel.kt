package com.rspl.rojgaarrakshak.landing.otp

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.google.gson.annotations.SerializedName
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.models.ResendSigninResponse.ResendsigninResponse
import com.rspl.rojgaarrakshak.models.VerifyOTPSignIn.SignInOtp
import com.rspl.rojgaarrakshak.models.VerifyOtpSignup.Signup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class OTPViewModel  @Inject constructor(
    private val repository: Repository
) : ViewModel() {


    var getOtpResponse = MutableLiveData<NetworkResult<SignInOtp>>()
    var resendotpResponse = MutableLiveData<NetworkResult<ResendsigninResponse>>()

    data class recendotpmodel( @SerializedName("phone_number" ) var phoneNumber : String? = null
    )
    fun verifyotpSignin(ctx:Context,mobilenumber:String,otp:String)
    {
        viewModelScope.launch(Dispatchers.IO+repository.getExceptionHandler(getOtpResponse)) {
            getOtpResponse.postValue(NetworkResult.Loading())
            val responce=repository.verifyOTPSignin(ReqModel(mobilenumber,otp))
            handleResponse(responce,getOtpResponse)
        }
    }

    fun Resendotpsignin(mobilenumber:String)
    {
        viewModelScope.launch(Dispatchers.IO+repository.getExceptionHandler(resendotpResponse)) {
            resendotpResponse.postValue(NetworkResult.Loading())
            val responce=repository.SigninResendOtp(recendotpmodel(mobilenumber))
            handleResponse(responce,resendotpResponse)
        }
    }

    private data class ReqModel(var phone_number : String, var otp : String)


}