package com.avsaverage.fooddeliveryapp.data.repositories

import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse
import com.avsaverage.fooddeliveryapp.util.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(db: DatabaseReference ) : FoodRepository {

    private val dbChild = db.child("food")
    override fun insert(item: FoodModelResponse.FoodItems): Flow<Resource<String>> = callbackFlow{
        trySend(Resource.Loading())

        dbChild.orderByKey().limitToLast(1).get().addOnSuccessListener { dataSnapshot ->
            var lastId = 0
            if (dataSnapshot.children.count() > 0) {
                val lastItem = dataSnapshot.children.first().getValue(FoodModelResponse.FoodItems::class.java)
                lastId = lastItem?.id ?: 0
            }

            item.id = if (lastId == 0) 1 else lastId + 1

            dbChild.orderByKey().limitToLast(1).get().addOnSuccessListener { dataSnapshot ->
                var lastKey = ""
                if (dataSnapshot.children.count() > 0) {
                    val lastItem = dataSnapshot.children.first()
                    lastKey = lastItem.key ?: ""
                }

                val newKey = if (lastKey.isEmpty()) "1" else (lastKey.toInt() + 1).toString()

                dbChild.child(newKey).setValue(item)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            trySend(Resource.Success("Данные добавлены успешно.."))
                        }
                    }
                    .addOnFailureListener {
                        trySend(Resource.Error(it.message))
                    }
            }
        }

        awaitClose {
            close()
        }
    }

    override fun getFood(): Flow<Resource<List<FoodModelResponse>>> = callbackFlow {
        trySend(Resource.Loading())
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map {
                    FoodModelResponse(
                        it.getValue(FoodModelResponse.FoodItems::class.java),
                        it.key
                    )
                }
                trySend(Resource.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Resource.Error(error.message))
            }
        }
        dbChild.addValueEventListener(valueEvent)
        awaitClose{
            dbChild.removeEventListener(valueEvent)
        }
    }

    override fun delete(key: String): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())
        dbChild.child(key).removeValue()
            .addOnCompleteListener {
                trySend(Resource.Success("Позиция удалена успешно.."))
            }
            .addOnFailureListener {
                trySend(Resource.Error(it.message))
            }
        awaitClose {
            close()
        }
    }

    override fun update(res: FoodModelResponse): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())
        val map = HashMap<String,Any>()
        map["description"] = res.food?.description!!
        map["id"] = res.food?.id!!
        map["image"] = res.food?.image!!
        map["name"] = res.food?.name!!
        map["price"] = res.food?.price!!

        dbChild.child(res.key!!).updateChildren(
            map
        ).addOnCompleteListener {
            trySend(Resource.Success("Позиция обновлена успешно.."))
        }.addOnFailureListener {
            trySend(Resource.Error(it.message))
        }
        awaitClose {
            close()
        }
    }

}