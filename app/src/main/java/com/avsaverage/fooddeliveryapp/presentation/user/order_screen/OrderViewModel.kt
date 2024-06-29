package com.avsaverage.fooddeliveryapp.presentation.user.order_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse
import com.avsaverage.fooddeliveryapp.data.models.OrderModelResponse
import com.avsaverage.fooddeliveryapp.data.models.UserDataResponse
import com.avsaverage.fooddeliveryapp.data.repositories.CartRepository
import com.avsaverage.fooddeliveryapp.data.repositories.FoodRepository
import com.avsaverage.fooddeliveryapp.data.repositories.OrderRepository
import com.avsaverage.fooddeliveryapp.data.repositories.UserDataRepository
import com.avsaverage.fooddeliveryapp.presentation.states.FoodState
import com.avsaverage.fooddeliveryapp.presentation.states.OrderState
import com.avsaverage.fooddeliveryapp.presentation.states.SaveSate
import com.avsaverage.fooddeliveryapp.presentation.states.UserDataState
import com.avsaverage.fooddeliveryapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel(){

    var selectedDate : MutableState<String> = mutableStateOf(LocalDate.now().toString())
    var selectedTime : MutableState<String> = mutableStateOf("${"%02d".format(LocalTime.now().hour)}:${"%02d".format(LocalTime.now().minute)}")
    var address: MutableState<String> = mutableStateOf("")
    var name : MutableState<String> = mutableStateOf("")
    var telephone : MutableState<String> = mutableStateOf("")

    private val _res: MutableState<FoodState> = mutableStateOf(FoodState())
    val res: State<FoodState> = _res

    private val _userDataState: MutableState<UserDataState> = mutableStateOf(UserDataState())
    val userDataState = _userDataState

    private val _state = Channel<OrderState>()
    val state = _state.receiveAsFlow()

    private val _saveState = Channel<SaveSate>()
    val saveSate = _saveState.receiveAsFlow()

    private val _orderModelResponse : MutableState<OrderModelResponse> = mutableStateOf(OrderModelResponse(
        isAccepted = false,
        isDelivered = false,
        isReady = false
    ))
    val orderModelResponse = _orderModelResponse

    init {
        viewModelScope.launch {
            userDataRepository.getUserData().collect { userData ->
                when (userData) {
                    is Resource.Success -> {
                        _userDataState.value = userData.data?.let {
                            UserDataState(
                                user = it
                            )
                        }!!
                        address.value = _userDataState.value.user?.address!!
                        name.value = _userDataState.value.user?.name!!
                        telephone.value = _userDataState.value.user?.telephone!!
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


    fun clickConfirm(orderModelResponse: OrderModelResponse) = viewModelScope.launch {
        orderRepository.createOrder(orderModelResponse).collect{result ->
            when(result)
            {
                is Resource.Success -> {
                    _state.send(OrderState(isSuccess = true))
                }
                is Resource.Loading -> {
                    _state.send(OrderState(isLoading = true))
                }
                is Resource.Error ->{
                    _state.send(OrderState(isError = result.message))
                }
            }
        }
    }

}