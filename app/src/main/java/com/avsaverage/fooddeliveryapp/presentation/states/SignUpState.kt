package com.avsaverage.fooddeliveryapp.presentation.states

data class SignUpState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: String? = ""

)