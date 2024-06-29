package com.avsaverage.fooddeliveryapp.data.models

data class FoodModelResponse(
    val food: FoodItems?,
    val key: String? = ""
)
{
    data class FoodItems(
        var id: Int? = 0,
        var name: String? = "",
        var description : String? = "",
        var image : String? = "",
        var price: Double? = 0.0
    )
}