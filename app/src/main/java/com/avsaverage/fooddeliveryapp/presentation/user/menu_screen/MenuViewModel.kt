package com.avsaverage.fooddeliveryapp.presentation.user.menu_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
class MenuViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val foodRepository: FoodRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _res: MutableState<FoodState> = mutableStateOf(FoodState())
    val res: State<FoodState> = _res

    private val _userDataState : MutableState<UserDataState> = mutableStateOf(UserDataState())
    val userDataState = _userDataState

    private val _saveState = Channel<SaveSate>()
    val saveSate = _saveState.receiveAsFlow()


    var selectedItem : FoodModelResponse.FoodItems? = null

    var isFoodAddDialogShown by mutableStateOf(false)
        private set

    fun onFoodClick(itemState:FoodModelResponse.FoodItems)
    {
        selectedItem = itemState
        isFoodAddDialogShown = true
    }

    fun onDismissDialog()
    {
        isFoodAddDialogShown = false
    }
    fun clickAddToCart(userDataResponse: UserDataResponse, foodItems: FoodModelResponse.FoodItems, quantity: Int)
    {
        _userDataState.value.user = cartRepository.addToCart(userDataResponse, foodItems, quantity)
    }


    fun updateUserData(userDataResponse: UserDataResponse) = viewModelScope.launch {
        userDataRepository.saveUserData(userDataResponse).collect{result ->
            when(result)
            {
                is Resource.Success ->{
                    _saveState.send(SaveSate(isSuccess = true))
                }
                is Resource.Loading ->{
                    _saveState.send(SaveSate(isLoading = true))
                }
                is Resource.Error ->{
                    _saveState.send(SaveSate(isError = result.message))
                }
            }
        }
    }
    init {
        viewModelScope.launch {
            foodRepository.getFood().collect {
                when (it) {
                    is Resource.Success -> {
                        _res.value = FoodState(
                            food = it.data
                        )
                    }

                    is Resource.Error -> {
                        _res.value = FoodState(
                            error = it.message.toString()
                        )
                    }

                    is Resource.Loading -> {
                        _res.value = FoodState(
                            isLoading = true
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
}