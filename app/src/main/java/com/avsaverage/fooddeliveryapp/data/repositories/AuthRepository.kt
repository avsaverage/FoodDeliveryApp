package com.avsaverage.fooddeliveryapp.data.repositories

import com.avsaverage.fooddeliveryapp.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(email: String, password: String) : Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String) : Flow<Resource<AuthResult>>


}