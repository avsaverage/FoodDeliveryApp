package com.avsaverage.fooddeliveryapp.data.repositories

import com.avsaverage.fooddeliveryapp.util.Resource
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class ImageUploadServiceImpl @Inject constructor(private val firebaseStorage: FirebaseStorage) : ImageUploadService {
    override fun updateImage(image: ByteArray, folderName: String): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading())
        try {
            val storageRef = firebaseStorage.reference.child("$folderName/${UUID.randomUUID()}")
            val uploadTask = storageRef.putBytes(image)

            val snapshot = uploadTask.await()

            val downloadUrl = snapshot.storage.downloadUrl.await()

            trySend(Resource.Success(downloadUrl.toString()))
            close()
        } catch (e: Exception) {
            trySend(Resource.Error(e.message ?: "Произошла ошибка при загрузке изображения"))
            close(e)
        }

    }

    override suspend fun deleteImageByAccessToken(accessToken: String) {
        try {
            val storageRef = firebaseStorage.getReferenceFromUrl(accessToken)
            storageRef.delete().await()
        } catch (e: Exception) {
            throw RuntimeException("Произошла ошибка при удалении изображения: ${e.message}")
        }
    }
}