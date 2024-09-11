package com.rspl.rojgaarrakshak.landing.pay_and_complete

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.google.gson.annotations.SerializedName
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.dashboard.settings.SettingsViewModel
import com.rspl.rojgaarrakshak.models.PaymentInisialRespose.PaymentInisialResponse
import com.rspl.rojgaarrakshak.models.PaymentSatusResponse.PaymentStatusResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject
@HiltViewModel
class WebViewVIewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {


    var paymentStatus=MutableLiveData<NetworkResult<PaymentStatusResponse>>()

    data class reqmodel(
        @SerializedName("transactionId" ) var transactionId : String? = null
    )



    fun PaymentStatus(token:String,message:String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(paymentStatus)) {
            paymentStatus.postValue(NetworkResult.Loading())
            val response = repository.PaymentSatus(token, reqmodel(message))
            handleResponse(response,paymentStatus)
        }
    }

}