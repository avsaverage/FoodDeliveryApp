package com.avsaverage.fooddeliveryapp.presentation.states

import com.avsaverage.fooddeliveryapp.data.models.OrderModelResponse

data class OrderList (
    val orders: List<OrderModelResponse>? = emptyList(),
    val isLoading: Boolean = false,
    val isError: String? = ""
)