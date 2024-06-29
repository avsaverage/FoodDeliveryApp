package com.avsaverage.fooddeliveryapp.data.repositories

import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse
import com.avsaverage.fooddeliveryapp.data.models.UserDataResponse
import com.avsaverage.fooddeliveryapp.util.Resource
import kotlinx.coroutines.flow.Flow


interface UserDataRepository {
    fun saveUserData(userData: UserDataResponse) : Flow<Resource<String>>
    fun getUserData() : Flow<Resource<UserDataResponse>>
    fun createUserData(userData: UserDataResponse) : Flow<Resource<String>>

}