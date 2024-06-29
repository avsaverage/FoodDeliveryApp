package com.avsaverage.fooddeliveryapp.di

import com.avsaverage.fooddeliveryapp.data.repositories.AuthRepository
import com.avsaverage.fooddeliveryapp.data.repositories.AuthRepositoryImpl
import com.avsaverage.fooddeliveryapp.data.repositories.CartRepository
import com.avsaverage.fooddeliveryapp.data.repositories.CartRepositoryImpl
import com.avsaverage.fooddeliveryapp.data.repositories.ImageUploadService
import com.avsaverage.fooddeliveryapp.data.repositories.ImageUploadServiceImpl
import com.avsaverage.fooddeliveryapp.data.repositories.OrderRepositoryImpl
import com.avsaverage.fooddeliveryapp.data.repositories.UserDataRepository
import com.avsaverage.fooddeliveryapp.data.repositories.UserDataRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesFirebaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebase() : FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun providesRealtimeDb() : DatabaseReference = Firebase.database.getReference()

    @Provides
    @Singleton
    fun providesCartRepositoryImpl() : CartRepository {
        return CartRepositoryImpl()
    }

    @Provides
    @Singleton
    fun providesRepositoryImpl(firebaseAuth: FirebaseAuth) : AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesImageUploaderService(firebaseStorage: FirebaseStorage) : ImageUploadService {
        return ImageUploadServiceImpl(firebaseStorage)
    }

    @Provides
    @Singleton
    fun providesUserDataRepositoryImpl(firebaseAuth: FirebaseAuth, db: DatabaseReference) : UserDataRepository {
        return UserDataRepositoryImpl(firebaseAuth, db)
    }

}