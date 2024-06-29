package com.avsaverage.fooddeliveryapp.data.repositories

import com.avsaverage.fooddeliveryapp.data.models.UserDataResponse
import com.avsaverage.fooddeliveryapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    db: DatabaseReference
) : UserDataRepository {
    private val currentUser = firebaseAuth.currentUser

    private val dbChild = db.child("users")
    override fun saveUserData(userData: UserDataResponse): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())
        val dbRef = dbChild
        dbRef.get()
            .addOnSuccessListener { snapshot ->
                var found = false
                snapshot.children.forEach { child ->
                    val existingUser = child.getValue(UserDataResponse::class.java)
                    if (existingUser?.uid == userData.uid) {
                        dbRef.child(child.key ?: "").setValue(userData)
                            .addOnSuccessListener {
                                trySend(Resource.Success("Данные обновлены успешно.."))
                            }
                            .addOnFailureListener { exception ->
                                trySend(Resource.Error(exception.message.orEmpty()))
                            }
                        found = true
                        return@forEach
                    }
                }
                if (!found) {
                    trySend(Resource.Error("Пользователь не найден"))
                }
            }
            .addOnFailureListener { exception ->
                trySend(Resource.Error(exception.message.orEmpty()))
            }
        awaitClose {
            close()
        }
    }

    override fun getUserData(): Flow<Resource<UserDataResponse>> = callbackFlow{
        if(currentUser != null)
        {
            val uid = currentUser.uid
            val valueEvent = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.children.find {it.child("uid").value == uid}?.getValue(UserDataResponse::class.java)
                    userData?.let {
                        trySend(Resource.Success(it))
                    } ?: trySend(Resource.Error("Пользователь не найден"))
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
        else
        {
            trySend(Resource.Error("Вы вошли как гость"))
        }
    }

    override fun createUserData(userData: UserDataResponse): Flow<Resource<String>> = callbackFlow {
        if(currentUser != null)
        {
            trySend(Resource.Loading())
            userData.uid = currentUser.uid
            userData.mail = currentUser.email
            userData.cart = emptyList()
            userData.address = ""
            userData.telephone = ""
            userData.name = ""

            dbChild.get().addOnSuccessListener {
                snapshot ->
                val currentCount = snapshot.childrenCount.toInt()

                dbChild.child(currentCount.toString()).setValue(userData).addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(Resource.Success("Данные добавлены успешно.."))
                }
                    .addOnFailureListener {
                        trySend(Resource.Error(it.message))
                    }
            }.addOnFailureListener {
                trySend(Resource.Error(it.message))
            }

            awaitClose {
                close()
            }
        }
    }
}