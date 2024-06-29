package com.avsaverage.fooddeliveryapp.presentation.signin_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avsaverage.fooddeliveryapp.data.repositories.AuthRepository
import com.avsaverage.fooddeliveryapp.presentation.states.UserDataState
import com.avsaverage.fooddeliveryapp.presentation.states.SignInState
import com.avsaverage.fooddeliveryapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository : AuthRepository,
) : ViewModel() {


    private val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    private val _isManager = mutableStateOf(false)
    val isManager = _isManager

    private val _authState : MutableState<UserDataState> = mutableStateOf(UserDataState())
    val authState : State<UserDataState> = _authState

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        if(email == "manager@gmail.com")
        {
            _isManager.value = true
        }
        authRepository.loginUser(email, password).collect{ result ->
            when(result)
            {
                is Resource.Success ->{
                    _signInState.send(SignInState(isSuccess = true))
                }
                is Resource.Loading ->{
                    _signInState.send(SignInState(isLoading = true))
                }
                is Resource.Error ->{
                    _signInState.send(SignInState(isError = result.message))
                }
            }
        }
    }
}