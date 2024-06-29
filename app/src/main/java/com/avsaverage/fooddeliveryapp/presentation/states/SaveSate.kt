package com.avsaverage.fooddeliveryapp.presentation.states

data class SaveSate (
    var isLoading : Boolean = false,
    var isSuccess : Boolean = false,
    var isError: String? = ""
)