package com.avsaverage.fooddeliveryapp.di

import androidx.lifecycle.ViewModel
import com.avsaverage.fooddeliveryapp.data.repositories.FoodRepository
import com.avsaverage.fooddeliveryapp.data.repositories.FoodRepositoryImpl
import com.avsaverage.fooddeliveryapp.data.repositories.OrderRepository
import com.avsaverage.fooddeliveryapp.data.repositories.OrderRepositoryImpl
import com.avsaverage.fooddeliveryapp.data.repositories.UserDataRepository
import com.avsaverage.fooddeliveryapp.data.repositories.UserDataRepositoryImpl
import com.avsaverage.fooddeliveryapp.presentation.user.order_screen.OrderViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesFoodRepository(
        repo: FoodRepositoryImpl
    ): FoodRepository
}
@Module
@InstallIn(ViewModelComponent::class)
abstract class OrderRepositoryModule {
    @Binds
    abstract fun providesOrderRepository(
        repo: OrderRepositoryImpl
    ): OrderRepository
}

