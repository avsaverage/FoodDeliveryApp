package com.avsaverage.fooddeliveryapp.data.repositories

import com.avsaverage.fooddeliveryapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ImageUploadService {
    fun updateImage(image: ByteArray, folderName: String): Flow<Resource<String>>
    suspend fun deleteImageByAccessToken(accessToken: String)
}