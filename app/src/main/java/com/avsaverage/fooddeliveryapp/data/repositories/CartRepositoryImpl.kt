package com.avsaverage.fooddeliveryapp.data.repositories

import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse
import com.avsaverage.fooddeliveryapp.data.models.UserDataResponse
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor() : CartRepository {
    override fun addToCart(userDataResponse: UserDataResponse, foodItem: FoodModelResponse.FoodItems, quantityRequest: Int,) : UserDataResponse  {
        val updatedCart = userDataResponse.cart?.toMutableList() ?: mutableListOf()
        val existingItem = updatedCart.find { it.id == foodItem.id }
        if (existingItem != null) {
            existingItem.quantity = (existingItem.quantity ?: 0) + quantityRequest
        } else {
            updatedCart.add(UserDataResponse.CartItem(id = foodItem.id, quantity = quantityRequest))
        }
        val updatedUserData = userDataResponse.copy(cart = updatedCart)
        return updatedUserData
    }

    override fun removeFromCart(userDataResponse: UserDataResponse, foodItem: FoodModelResponse.FoodItems) : UserDataResponse {
        val existingItem = userDataResponse.cart?.find { it.id == foodItem.id }
        existingItem?.let {
            val updatedCart = (userDataResponse.cart ?: mutableListOf()).filter { it.id != foodItem.id }
            userDataResponse.cart = if (updatedCart.isNotEmpty()) updatedCart else null
        }
        return userDataResponse
    }

    override fun getCartItems(userDataResponse: UserDataResponse, foodItems: List<FoodModelResponse>): List<Pair<FoodModelResponse, Int>>? {
        if (userDataResponse.cart.isNullOrEmpty()) {
            return null
        }
        val foodItemsMap = foodItems.associateBy { it.food?.id }
        return userDataResponse.cart?.mapNotNull { cartItem ->
            foodItemsMap[cartItem.id]?.let { foodItem ->
                Pair(foodItem, cartItem.quantity ?: 0)
            }
        }
    }
}