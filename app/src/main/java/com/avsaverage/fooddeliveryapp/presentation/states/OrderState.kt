package com.avsaverage.fooddeliveryapp.presentation.states

data class OrderState (
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isError: String? = ""
)
