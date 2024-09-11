package com.rspl.rojgaarrakshak.landing.sign_in

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.models.SigninResponse.SigninResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {


    var getOtpResponse = MutableLiveData<NetworkResult<SigninResponse>>()
    fun requestSignIn(ctx: Context, mobileNumber : String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(getOtpResponse)) {
            getOtpResponse.postValue(NetworkResult.Loading())
            val response = repository.getOtpOnMobileNumber(ReqModel(mobileNumber))
            handleResponse(response,getOtpResponse)
        }

    }

    private data class ReqModel(var phone_number : String)


}