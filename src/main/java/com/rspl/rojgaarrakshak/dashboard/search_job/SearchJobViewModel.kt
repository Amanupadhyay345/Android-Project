package com.rspl.rojgaarrakshak.dashboard.search_job

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.google.gson.annotations.SerializedName
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.dashboard.settings.SettingsViewModel
import com.rspl.rojgaarrakshak.models.ApplyJobResponse.AppyJob
import com.rspl.rojgaarrakshak.models.GetappliedJobResponse.AppliedJobs
import com.rspl.rojgaarrakshak.models.GetappliedJobResponse.GetAppliedJobResponse
import com.rspl.rojgaarrakshak.models.PaymentInisialRespose.PaymentInisialResponse
import com.rspl.rojgaarrakshak.models.PaymentSatusResponse.PaymentStatusResponse
import com.rspl.rojgaarrakshak.models.search_response.JobsData
import com.rspl.rojgaarrakshak.models.search_response.SearchResponse
import com.rspl.rojgaarrakshak.models.search_suggestion.SearchSuggestionResponse
import com.rspl.rojgaarrakshak.models.state_response.StatesData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class SearchJobViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

//    init {
//        findJobs("", "")
//
//    }

    var selectedLocation =""
    var searchSuggestionArray : ArrayList<String> = arrayListOf()
    var searchResultArray : ArrayList<JobsData> = arrayListOf()
    val paymentinisilalResponse=  MutableLiveData<NetworkResult<PaymentInisialResponse>>()


    val jobidlist = arrayListOf<JobsData>()
    var suggestionItemClicked = false
    var jobSearchResponse = MutableLiveData<NetworkResult<SearchResponse>>()
    var applyjobresponse=MutableLiveData<NetworkResult<AppyJob>>()
    var getappliedjob = MutableLiveData<NetworkResult<GetAppliedJobResponse>>()

     data class ReqModelSearch(@SerializedName("job_title" ) var jobTitle : String? = null,
                               @SerializedName("location"  ) var location : String? = null)

    data class ApplyJobRequestModel(
        @SerializedName("job_id" ) var jobId : String? = null
    )

    fun findJobs(searchText: String, location: String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(jobSearchResponse)) {
            Log.e("findJobs","searchText=> $searchText, location=> $location")
            jobSearchResponse.postValue(NetworkResult.Loading())
            val response = repository.searchJob(ReqModelSearch(searchText,location))
            handleResponse(response,jobSearchResponse)
        }
    }

    fun InisialPayment(Token:String)
    {
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(paymentinisilalResponse)) {
            paymentinisilalResponse.postValue(NetworkResult.Loading())
            val response = repository.InsilialPayment(Token)
            handleResponse(response,paymentinisilalResponse)
        }
    }
    fun ApplyJob(token:String,id:String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(applyjobresponse)) {
            applyjobresponse.postValue(NetworkResult.Loading())
            val response = repository.applyjob(token, ApplyJobRequestModel(id))

            handleResponse(response,applyjobresponse)
        }

    }

    fun getapppliedjob(Token:String)
    {
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(getappliedjob)) {
            getappliedjob.postValue(NetworkResult.Loading())
            val response = repository.getAppliedjob(Token)
            handleResponse(response,getappliedjob)
        }
    }

    var searchSuggestionResponse = MutableLiveData<NetworkResult<SearchSuggestionResponse>>()
    private data class ReqModelSuggestion(var search_param: String)
    fun searchSuggestion(searchText : String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(searchSuggestionResponse)) {
            searchSuggestionResponse.postValue(NetworkResult.Loading())
            val response = repository.getSearchSuggestion(ReqModelSuggestion(searchText))
            handleResponse(response,searchSuggestionResponse)
        }
    }

    var paymentStatusResponse=MutableLiveData<NetworkResult<PaymentStatusResponse>>()

    fun PaymentStatus(token:String,message:String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(paymentStatusResponse)) {
            paymentStatusResponse.postValue(NetworkResult.Loading())
            val response = repository.PaymentSatus(token, SettingsViewModel.reqmodel(message))
            handleResponse(response,paymentStatusResponse)
        }
    }

}