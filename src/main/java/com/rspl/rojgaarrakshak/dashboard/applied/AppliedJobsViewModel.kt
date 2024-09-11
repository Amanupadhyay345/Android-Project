package com.rspl.rojgaarrakshak.dashboard.applied

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.models.GetappliedJobResponse.AppliedJobs
import com.rspl.rojgaarrakshak.models.GetappliedJobResponse.GetAppliedJobResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppliedJobsViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    var getappliedjob = MutableLiveData<NetworkResult<GetAppliedJobResponse>>()
    var appliedjobresult : ArrayList<AppliedJobs> = arrayListOf()

    fun getapppliedjob(Token:String)
    {
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(getappliedjob)) {
            getappliedjob.postValue(NetworkResult.Loading())
            val response = repository.getAppliedjob(Token)
            handleResponse(response,getappliedjob)
        }
    }
}