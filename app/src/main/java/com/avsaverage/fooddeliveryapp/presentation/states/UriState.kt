package com.avsaverage.fooddeliveryapp.presentation.states

data class UriState (
    var isSuccess: String? = "",
    var isLoading: Boolean = false,
    var isError: String? = ""
)