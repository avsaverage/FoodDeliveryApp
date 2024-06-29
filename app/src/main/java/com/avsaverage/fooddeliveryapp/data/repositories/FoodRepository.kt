package com.avsaverage.fooddeliveryapp.data.repositories

import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse
import com.avsaverage.fooddeliveryapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    fun insert(
        item: FoodModelResponse.FoodItems
    ) : Flow<Resource<String>>
    fun getFood() : Flow<Resource<List<FoodModelResponse>>>

    fun delete(
        key:String
    ) : Flow<Resource<String>>

    fun update(
        res:FoodModelResponse
    ) : Flow<Resource<String>>
}