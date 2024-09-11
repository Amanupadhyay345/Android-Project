package com.rspl.rojgaarrakshak.dashboard.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.models.CityResponce.CitiesData
import com.rspl.rojgaarrakshak.models.CityResponce.CityResponce
import com.rspl.rojgaarrakshak.models.state_response.StateResponse
import com.rspl.rojgaarrakshak.models.state_response.StatesData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    init {
        getStates()
    }

    val statesResponse = MutableLiveData<NetworkResult<StateResponse>>()
    val cityResponce = MutableLiveData<NetworkResult<CityResponce>>()
    val stateList = arrayListOf<StatesData>()
    val citylist = arrayListOf<CitiesData>()

    fun getStates() {

        viewModelScope.launch(Dispatchers.IO + repository.getExceptionHandler(statesResponse)) {
            statesResponse.postValue(NetworkResult.Loading())
            val response = repository.getState()
            handleResponse(response, statesResponse)
        }

    }

    fun getcity(body: Any) {
        viewModelScope.launch(Dispatchers.IO + repository.getExceptionHandler(cityResponce)) {
            cityResponce.postValue(NetworkResult.Loading())
            val response = repository.getcity(body)
            handleResponse(response, cityResponce)

        }
    }


}