package com.rspl.rojgaarrakshak.landing.UserLocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.google.gson.annotations.SerializedName
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.models.AddJobPrefrencesModel.AddJobPrefrences
import com.rspl.rojgaarrakshak.models.CityResponce.CitiesData
import com.rspl.rojgaarrakshak.models.CityResponce.CityResponce
import com.rspl.rojgaarrakshak.models.state_response.StateResponse
import com.rspl.rojgaarrakshak.models.state_response.StatesData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLocationViewModel @Inject constructor(

    private var repository: Repository

) :ViewModel(){

    val allStateResponse= MutableLiveData<NetworkResult<StateResponse>>()
   val allCityResponse = MutableLiveData<NetworkResult<CityResponce>>()

    val allSaveCityResponse = MutableLiveData<NetworkResult<CityResponce>>()

    val stateList = arrayListOf<StatesData>()
    val citylist= arrayListOf<CitiesData>()

    val saveCitydata = arrayListOf<CitiesData>()

    var saveSkillResponse = MutableLiveData<NetworkResult<AddJobPrefrences>>()

    data class AddJobReqModel(

        @SerializedName("pre_skills"     ) var preSkills    : String? = null,
        @SerializedName("pre_sub_skills" ) var preSubSkills : String? = null,
        @SerializedName("location"       ) var location     : String? = null,
        @SerializedName("state_id"       ) var stateId      : String? = null
    )

    fun getAllState(){
        viewModelScope.launch (Dispatchers.IO+repository.getExceptionHandler(allStateResponse)) {
            allStateResponse.postValue(NetworkResult.Loading())
            val response=repository.getState()
            handleResponse(response,allStateResponse)
        }
    }

    fun getAllCity(body:Any)
    {
        viewModelScope.launch(Dispatchers.IO+repository.getExceptionHandler(allCityResponse)){
            allCityResponse.postValue(NetworkResult.Loading())
            val response=repository.getcity(body)
            handleResponse(response,allCityResponse)
        }
    }


    fun getSaveCity(body:Any)
    {
        viewModelScope.launch(Dispatchers.IO+repository.getExceptionHandler(allSaveCityResponse)){
            allSaveCityResponse.postValue(NetworkResult.Loading())
            val response=repository.getcity(body)
            handleResponse(response,allSaveCityResponse)
        }
    }

    fun saveSkillPrefrences(token:String,skill:String,subskill:String,location:String,stateId:String)
    {
        viewModelScope.launch(Dispatchers.IO+repository.getExceptionHandler(saveSkillResponse))
        {
            saveSkillResponse.postValue(NetworkResult.Loading())
            val response=repository.addJobPerformances(token,AddJobReqModel
                (skill,subskill,location,stateId))
            handleResponse(response,saveSkillResponse)
        }
    }
}