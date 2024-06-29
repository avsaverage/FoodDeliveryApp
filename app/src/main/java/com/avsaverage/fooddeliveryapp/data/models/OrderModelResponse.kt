package com.avsaverage.fooddeliveryapp.data.models

data class OrderModelResponse(
    var uid: String? = null,
    var orderItems: List<UserDataResponse.CartItem>? = emptyList(),
    var name: String? = null,
    var date: String? = null,
    var time: String? = null,
    var address: String? = null,
    var telephone: String? = null,
    var isAccepted: Boolean? = null,
    var isReady: Boolean? = null,
    var isDelivered: Boolean? = null
    )
