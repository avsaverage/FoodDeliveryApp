package com.avsaverage.fooddeliveryapp.presentation.signup_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avsaverage.fooddeliveryapp.data.models.UserDataResponse
import com.avsaverage.fooddeliveryapp.data.repositories.AuthRepository
import com.avsaverage.fooddeliveryapp.data.repositories.UserDataRepository
import com.avsaverage.fooddeliveryapp.presentation.states.SignUpState
import com.avsaverage.fooddeliveryapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository : AuthRepository,
    private val dataRepository: UserDataRepository
) : ViewModel() {

    val _signUpState = Channel<SignUpState>()
    val signUpState = _signUpState.receiveAsFlow()

    val _createState = Channel<SignUpState>()
    val createState = _createState.receiveAsFlow()

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            repository.registerUser(email, password).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _signUpState.send(SignUpState(isSuccess = true))
                    }

                    is Resource.Loading -> {
                        _signUpState.send(SignUpState(isLoading = true))
                    }

                    is Resource.Error -> {
                        _signUpState.send(SignUpState(isError = result.message))
                    }

                }
            }
        }
    }

    fun createUser() {
        viewModelScope.launch {
            dataRepository.createUserData(UserDataResponse()).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _createState.send(SignUpState(isSuccess = true))
                    }

                    is Resource.Loading -> {
                        _createState.send(SignUpState(isLoading = true))
                    }

                    is Resource.Error -> {
                        _createState.send(SignUpState(isError = result.message))
                    }

                }
            }
        }
    }
}

