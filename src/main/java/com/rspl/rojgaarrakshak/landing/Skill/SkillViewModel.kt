package com.rspl.rojgaarrakshak.landing.Skill

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
import com.rspl.rojgaarrakshak.models.GetSubSkillModel.SubSkillResponse
import com.rspl.rojgaarrakshak.models.SkillModel.UserSkillResponse
import com.rspl.rojgaarrakshak.models.state_response.StateResponse
import com.rspl.rojgaarrakshak.models.state_response.StatesData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkillViewModel @Inject constructor(

    private val repository: Repository

) : ViewModel() {
    var getUserSkill = MutableLiveData<NetworkResult<UserSkillResponse>>()

    var UserSubSkillResponse=MutableLiveData<NetworkResult<SubSkillResponse>>()

    fun getSKillData(){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(getUserSkill)) {
            getUserSkill.postValue(NetworkResult.Loading())
            val response = repository.Getuserskill()
            handleResponse(response,getUserSkill)
        }
    }
    fun getUserSubSkill(id:String){
        viewModelScope.launch(Dispatchers.IO+ repository.getExceptionHandler(UserSubSkillResponse)) {
            UserSubSkillResponse.postValue(NetworkResult.Loading())
            val response = repository.GetUserSubskill(id)
            handleResponse(response,UserSubSkillResponse)
        }
    }

}