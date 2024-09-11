package com.rspl.rojgaarrakshak.dashboard.work_history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.models.WorkHistoryResponse.WorkHistory
import com.rspl.rojgaarrakshak.models.WorkHistoryResponse.WorkHistoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkHistoryViewModel @Inject constructor(
    private val repository: Repository


): ViewModel() {

    var workhistoryresponse = MutableLiveData<NetworkResult<WorkHistoryResponse>>()
    var WorkHistorydata : ArrayList<WorkHistory> = arrayListOf()


    fun GetWorkhistory(Token:String)
    {
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(workhistoryresponse)) {
            workhistoryresponse.postValue(NetworkResult.Loading())
            val response = repository.workhistory(Token)
            handleResponse(response,workhistoryresponse)
        }
    }
}