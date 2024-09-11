package com.rspl.rojgaarrakshak.landing.pay_and_complete

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
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
class PayAndCompleteViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

   val paymentinisilalResponse=  MutableLiveData<NetworkResult<PaymentInisialResponse>>()

    var paymentStatusResponse=MutableLiveData<NetworkResult<PaymentStatusResponse>>()

    fun InisialPayment(Token:String)
    {
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(paymentinisilalResponse)) {
            paymentinisilalResponse.postValue(NetworkResult.Loading())
            val response = repository.InsilialPayment(Token)
            handleResponse(response,paymentinisilalResponse)
        }
    }

    fun PaymentStatus(token:String,message:String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(paymentStatusResponse)) {
            paymentStatusResponse.postValue(NetworkResult.Loading())
            val response = repository.PaymentSatus(token, SettingsViewModel.reqmodel(message))
            handleResponse(response,paymentStatusResponse)
        }
    }




}