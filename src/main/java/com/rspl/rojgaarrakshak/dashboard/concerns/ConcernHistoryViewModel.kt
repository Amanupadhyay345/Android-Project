package com.rspl.rojgaarrakshak.dashboard.concerns

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.peerpicks.core.extensions.handleResponse
import com.rspl.rojgaarrakshak.NetworkLayer.NetworkResult
import com.rspl.rojgaarrakshak.NetworkLayer.Repository
import com.rspl.rojgaarrakshak.models.ConcernHistoryResponse.ConcernData
import com.rspl.rojgaarrakshak.models.ConcernHistoryResponse.ConcernHistoryResponce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConcernHistoryViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var ConcernHistoryResponse = MutableLiveData<NetworkResult<ConcernHistoryResponce>>()

    var ConcernHistoryData: ArrayList<ConcernData> = arrayListOf()

    fun ConcernHistory(token: String) {
        viewModelScope.launch(Dispatchers.IO + repository.getExceptionHandler(ConcernHistoryResponse)) {
            ConcernHistoryResponse.postValue(NetworkResult.Loading())
            val response = repository.Concernhistory(token)

            handleResponse(response, ConcernHistoryResponse)
        }
    }

}