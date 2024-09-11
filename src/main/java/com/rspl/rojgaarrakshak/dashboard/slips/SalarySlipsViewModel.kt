package com.rspl.rojgaarrakshak.dashboard.slips

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.google.gson.annotations.SerializedName
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.models.GetSalarySlipResponse.GetSalarySlipResponse
import com.rspl.rojgaarrakshak.models.GetSalarySlipResponse.SalarySlipData
import com.rspl.rojgaarrakshak.models.Get_All_Job_Response.Data
import com.rspl.rojgaarrakshak.models.Get_All_Job_Response.GetjobResponce
import com.rspl.rojgaarrakshak.models.GetallyearsResponse.GETallyearsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalarySlipsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {



    val joblist = arrayListOf<Data>()

    var salaryslpresult:ArrayList<SalarySlipData> = arrayListOf()

    val alljobresponce= MutableLiveData<NetworkResult<GetjobResponce>>()
    var GetAllJobYear = MutableLiveData<NetworkResult<GETallyearsResponse>>()

    var yearlist= arrayListOf<com.rspl.rojgaarrakshak.models.GetallyearsResponse.Data>()
    var GetSalaryslip=MutableLiveData<NetworkResult<GetSalarySlipResponse>>()


    fun getAlljob(token:String){

        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(alljobresponce)) {
            alljobresponce.postValue(NetworkResult.Loading())
            val response = repository.getallJobs(token)
            handleResponse(response,alljobresponce)
        }

    }


    fun getjobyear(token:String,message:String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(GetAllJobYear)) {
            GetAllJobYear.postValue(NetworkResult.Loading())
            val response = repository.Getallyears(token,CreateConcernModel(message))

            handleResponse(response,GetAllJobYear)
        }

    }

    fun getSalarySlip(token:String,empid:String,year:String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(GetSalaryslip)) {
            GetSalaryslip.postValue(NetworkResult.Loading())
            val response = repository.GetSalarySlip(token,SalarySlip(empid,year))

            handleResponse(response,GetSalaryslip)
        }

    }



    data class CreateConcernModel(
        @SerializedName("emp_id" ) var empId : String? = null
    )

    data class SalarySlip(
        @SerializedName("emp_id" ) var empId : String? = null,
        @SerializedName("year"   ) var year  : String? = null
    )


}