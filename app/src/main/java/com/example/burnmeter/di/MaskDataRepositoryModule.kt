package com.example.burnmeter.di

import com.example.burnmeter.data.mask.MaskDataRepositoryImpl
import com.example.burnmeter.domain.repository.MaskDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class MaskDataRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindMaskDataRepository(impl: MaskDataRepositoryImpl): MaskDataRepository
}