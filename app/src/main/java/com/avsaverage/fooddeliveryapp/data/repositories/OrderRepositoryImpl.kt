package com.avsaverage.fooddeliveryapp.data.repositories

import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse
import com.avsaverage.fooddeliveryapp.data.models.OrderModelResponse
import com.avsaverage.fooddeliveryapp.util.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(db: DatabaseReference) : OrderRepository {

    private val dbChild = db.child("orders")
    override fun createOrder(orderModelResponse: OrderModelResponse): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())
        dbChild.get().addOnSuccessListener {
            snapshot ->
            val currentCount = snapshot.childrenCount.toInt()
            val countValue = if (currentCount > 0) currentCount.toString() else "0"
            dbChild.child(countValue).setValue(
                orderModelResponse
            ).addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(Resource.Success("Заказ оформлен успешно..."))
            }.addOnFailureListener {
                trySend(Resource.Error(it.message))
            }
        }.addOnFailureListener {
            trySend(Resource.Error(it.message))
        }
        awaitClose{
            close()
        }

    }

    override fun getOrders(): Flow<Resource<List<OrderModelResponse>>> = callbackFlow {
        trySend(Resource.Loading())

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<OrderModelResponse>()
                for (childSnapshot in snapshot.children) {
                    val order = childSnapshot.getValue(OrderModelResponse::class.java)
                    order?.let { items.add(it) }
                }
                trySend(Resource.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }
        }

        dbChild.addValueEventListener(valueEvent)

        awaitClose {
            dbChild.removeEventListener(valueEvent)
        }
    }

    override fun changeStateOrder(
        orderModelResponse: OrderModelResponse,
        state: String
    ): Flow<Resource<String>> = callbackFlow{
        trySend(Resource.Loading())

        dbChild.orderByChild("uid").equalTo(orderModelResponse.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val key = child.key
                        val updates = when (state) {
                            "accepted" -> mapOf("accepted" to true)
                            "ready" -> mapOf("ready" to true)
                            "delivered" -> mapOf("delivered" to true)
                            else -> {
                                return@addOnSuccessListener
                            }
                        }

                        dbChild.child(key!!).updateChildren(updates)
                            .addOnSuccessListener {
                                trySend(Resource.Success("Состояние заказа обновлено"))
                            }
                            .addOnFailureListener { exception ->
                                trySend(Resource.Error("Ошибка при обновлении состояния заказа: ${exception.message}"))
                            }
                    }
                } else {
                    trySend(Resource.Error("Заказ не найден в базе данных"))
                }
            }
            .addOnFailureListener { exception ->
                trySend(Resource.Error("Ошибка при поиске заказа: ${exception.message}"))
            }

        awaitClose {
        }
    }
}