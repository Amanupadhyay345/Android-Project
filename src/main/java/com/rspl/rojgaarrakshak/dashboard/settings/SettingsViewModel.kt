package com.rspl.rojgaarrakshak.dashboard.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.google.gson.annotations.SerializedName
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.models.RequestForRefund.RequestforRefundResponse
import com.rspl.rojgaarrakshak.models.RequestforAccountDeactivation.RequestforAccountDeactivationResponse
import com.rspl.rojgaarrakshak.models.search_response.SearchResponse
import com.rspl.rojgaarrakshak.models.skill_response.SkillResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var AccountDeactivation = MutableLiveData<NetworkResult<RequestforAccountDeactivationResponse>>()
    var RequestRefundResponce = MutableLiveData<NetworkResult<RequestforRefundResponse>>()

    data class reqmodel(
        @SerializedName("transactionId" ) var transactionId : String? = null
    )
    fun DeleteAccount(token:String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(AccountDeactivation)) {
            AccountDeactivation.postValue(NetworkResult.Loading())
            val response = repository.DeleteAccount(token)
            handleResponse(response,AccountDeactivation)
        }
    }
    fun RequestForRefund(token:String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(RequestRefundResponce)) {
            RequestRefundResponce.postValue(NetworkResult.Loading())
            val response = repository.RequestforRefund(token)
            handleResponse(response,RequestRefundResponce)
        }
    }
}