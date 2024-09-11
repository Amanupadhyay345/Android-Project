package com.rspl.rojgaarrakshak.models.state_response

data class StateResponse(
    val message: String?,
    val states_data: List<StatesData>?,
    val status: Boolean?
)