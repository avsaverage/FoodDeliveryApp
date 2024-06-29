package com.avsaverage.fooddeliveryapp.presentation.states

import com.avsaverage.fooddeliveryapp.data.models.UserDataResponse

data class UserDataState(
    var user: UserDataResponse? = null,
    val error: String = "",
    val isLoading: Boolean = false
)