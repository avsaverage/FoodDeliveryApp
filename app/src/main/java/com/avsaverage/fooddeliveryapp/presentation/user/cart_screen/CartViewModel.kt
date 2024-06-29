package com.avsaverage.fooddeliveryapp.presentation.user.cart_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse
import com.avsaverage.fooddeliveryapp.data.models.UserDataResponse
import com.avsaverage.fooddeliveryapp.data.repositories.CartRepository
import com.avsaverage.fooddeliveryapp.data.repositories.FoodRepository
import com.avsaverage.fooddeliveryapp.data.repositories.UserDataRepository
import com.avsaverage.fooddeliveryapp.presentation.states.FoodState
import com.avsaverage.fooddeliveryapp.presentation.states.SaveSate
import com.avsaverage.fooddeliveryapp.presentation.states.UserDataState
import com.avsaverage.fooddeliveryapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val foodRepository: FoodRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _res: MutableState<FoodState> = mutableStateOf(FoodState())
    val res: State<FoodState> = _res

    private val _userDataState: MutableState<UserDataState> = mutableStateOf(UserDataState())
    val userDataState = _userDataState

    private val _cartItems: MutableState<List<Pair<FoodModelResponse, Int>>?> = mutableStateOf(null)
    val cartItems: State<List<Pair<FoodModelResponse, Int>>?> = _cartItems

    private val _cartTotal: MutableState<Double> = mutableStateOf(0.0)
    val cartTotal: State<Double> = _cartTotal

    private val _saveState = Channel<SaveSate>()
    val saveSate = _saveState.receiveAsFlow()

    init {
        viewModelScope.launch {
            foodRepository.getFood().collect { item ->
                when (item) {
                    is Resource.Success -> {
                        _res.value = FoodState(
                            food = item.data
                        )
                    }

                    is Resource.Loading -> {
                        _res.value = FoodState(
                            isLoading = true
                        )
                    }

                    is Resource.Error -> {
                        _res.value = FoodState(
                            error = item.message.toString()
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            userDataRepository.getUserData().collect { userData ->
                when (userData) {
                    is Resource.Success -> {
                        _userDataState.value = userData.data?.let {
                            UserDataState(
                                user = it
                            )
                        }!!
                        _userDataState.value.user?.let {
                            _res.value.food?.let { it1 ->
                                updateCartItems(
                                    it,
                                    it1
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _userDataState.value = UserDataState(
                            isLoading = true
                        )
                    }

                    is Resource.Error -> {
                        _userDataState.value = UserDataState(
                            error = userData.message.toString()
                        )
                    }
                }
            }
        }
    }

    fun updateUserData(userDataResponse: UserDataResponse) = viewModelScope.launch {
        userDataRepository.saveUserData(userDataResponse).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _saveState.send(SaveSate(isSuccess = true))
                }

                is Resource.Loading -> {
                    _saveState.send(SaveSate(isLoading = true))
                }

                is Resource.Error -> {
                    _saveState.send(SaveSate(isError = result.message))
                }
            }
        }
    }

    private fun updateCartTotal() {
        var total = 0.0
        _cartItems.value?.forEach { (item, quantity) ->
            total += item.food?.price?.times(quantity) ?: 0.0
        }
        _cartTotal.value = total
    }

    private fun updateCartItems(
        userDataResponse: UserDataResponse,
        foodItems: List<FoodModelResponse>
    ) {
        _cartItems.value = cartRepository.getCartItems(userDataResponse, foodItems)
        updateCartTotal()
    }

    fun deleteFromCart(userDataResponse: UserDataResponse, foodItem: FoodModelResponse.FoodItems) {
        _userDataState.value.user = cartRepository.removeFromCart(userDataResponse, foodItem)
    }

}