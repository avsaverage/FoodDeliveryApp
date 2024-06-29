package com.avsaverage.fooddeliveryapp.data.models

data class UserDataResponse(
    var address: String? = "",
    var cart: List<CartItem>? = null,
    var mail:String? = "",
    var name:String? = "",
    var telephone:String? = "",
    var uid : String? = ""
){
    data class CartItem(
        var id: Int? = 0,
        var quantity : Int? = 0
    )
}
