package com.rspl.rojgaarrakshak.dashboard.ticket_tab

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.google.gson.reflect.TypeToken
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.models.GetUserDetails.UserDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var profilepicture:String=""


    var getuserdata = MutableLiveData<NetworkResult<UserDataResponse>>()

    
    fun getuserdata(token: String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(getuserdata)) {
            getuserdata.postValue(NetworkResult.Loading())
            val response = repository.Getuserdata(token)
            handleResponse(response,getuserdata)
        }
    }
}