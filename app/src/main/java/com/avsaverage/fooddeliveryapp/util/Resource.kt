package com.avsaverage.fooddeliveryapp.util

sealed class Resourse<T>(val data:T? = null, val message: String? = null) {
    class Success
}