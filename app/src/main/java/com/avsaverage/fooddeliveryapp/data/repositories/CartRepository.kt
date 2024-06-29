package com.avsaverage.fooddeliveryapp.data.repositories

import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse
import com.avsaverage.fooddeliveryapp.data.models.UserDataResponse

interface CartRepository {
    fun addToCart(userDataResponse: UserDataResponse, foodItem : FoodModelResponse.FoodItems, quantityRequest: Int) : UserDataResponse

    fun removeFromCart(userDataResponse: UserDataResponse, foodItem: FoodModelResponse.FoodItems) : UserDataResponse

    fun getCartItems(userDataResponse: UserDataResponse, foodItems: List<FoodModelResponse>) : List<Pair<FoodModelResponse, Int>>?
}