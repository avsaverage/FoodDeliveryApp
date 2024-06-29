package com.avsaverage.fooddeliveryapp.presentation.states

import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse

data class FoodState(
    val food: List<FoodModelResponse>? = emptyList(),
    val error:String = "",
    val isLoading:Boolean = false
)