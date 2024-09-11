package com.rspl.rojgaarrakshak.dashboard.concerns

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.google.gson.annotations.SerializedName
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.models.CreateConcernResponse.ConcernResponce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RaiseConcernViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    var CreteConcernResponce = MutableLiveData<NetworkResult<ConcernResponce>>()

    fun CreateConcern(token: String, message: String) {
        viewModelScope.launch(Dispatchers.IO + repository.getExceptionHandler(CreteConcernResponce)) {
            CreteConcernResponce.postValue(NetworkResult.Loading())
            val response = repository.Createconcern(token, CreateConcernModel(message))

            handleResponse(response, CreteConcernResponce)
        }

    }

    data class CreateConcernModel(
        @SerializedName("message") var message: String? = null
    )

}