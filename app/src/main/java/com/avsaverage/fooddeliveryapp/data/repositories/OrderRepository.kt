package com.avsaverage.fooddeliveryapp.data.repositories

import com.avsaverage.fooddeliveryapp.data.models.OrderModelResponse
import com.avsaverage.fooddeliveryapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun createOrder(orderModelResponse: OrderModelResponse) : Flow<Resource<String>>

    fun getOrders() : Flow<Resource<List<OrderModelResponse>>>

    fun changeStateOrder(orderModelResponse: OrderModelResponse, state : String) : Flow<Resource<String>>
}