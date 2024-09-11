package com.rspl.rojgaarrakshak.models.search_suggestion

data class SearchSuggestionResponse(
    val job_titles: List<String>?,
    val message: String?,
    val status: Boolean?
)