package com.avsaverage.fooddeliveryapp.presentation.manager.order_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avsaverage.fooddeliveryapp.data.models.OrderModelResponse
import com.avsaverage.fooddeliveryapp.data.repositories.OrderRepository
import com.avsaverage.fooddeliveryapp.presentation.states.FoodState
import com.avsaverage.fooddeliveryapp.presentation.states.OrderList
import com.avsaverage.fooddeliveryapp.presentation.states.SaveSate
import com.avsaverage.fooddeliveryapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.utils.io.makeShared
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderScreenManagerViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _res: MutableState<OrderList> = mutableStateOf(OrderList())
    val res: State<OrderList> = _res

    private val _state: MutableState<SaveSate> = mutableStateOf(SaveSate())
    val state = _state
    fun onStateChangeClick(orderState: OrderModelResponse)
    {
        if(!orderState.isAccepted!!)
        {
            viewModelScope.launch {
                orderRepository.changeStateOrder(orderState,"accepted").collect{
                    when(it)
                    {
                        is Resource.Success -> {
                            _state.value.isSuccess = true
                        }

                        is Resource.Error -> {
                            _state.value.isError = it.message
                        }

                        is Resource.Loading -> {
                            _state.value.isLoading = true
                        }
                    }
                }
            }
        }
        else if(!orderState.isReady!!)
        {

            viewModelScope.launch {
                orderRepository.changeStateOrder(orderState,"ready").collect{
                    when(it)
                    {
                        is Resource.Success -> {
                            _state.value.isSuccess = true
                        }

                        is Resource.Error -> {
                            _state.value.isError = it.message
                        }

                        is Resource.Loading -> {
                            _state.value.isLoading = true
                        }
                    }
                }
            }
        }
        else
        {
            viewModelScope.launch {
                orderRepository.changeStateOrder(orderState,"delivered").collect{
                    when(it)
                    {
                        is Resource.Success -> {
                            _state.value.isSuccess = true
                        }

                        is Resource.Error -> {
                            _state.value.isError = it.message
                        }

                        is Resource.Loading -> {
                            _state.value.isLoading = true
                        }
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            orderRepository.getOrders().collect {
                when (it) {
                    is Resource.Success -> {
                        _res.value = OrderList(
                            orders = it.data
                        )
                    }

                    is Resource.Error -> {
                        _res.value = OrderList(
                            isError = it.message.toString()
                        )
                    }

                    is Resource.Loading -> {
                        _res.value = OrderList(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }

}