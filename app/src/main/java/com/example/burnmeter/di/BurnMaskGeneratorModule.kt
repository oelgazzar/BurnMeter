package com.example.burnmeter.di

import com.example.burnmeter.data.mask.BurnMaskGeneratorImpl
import com.example.burnmeter.domain.mask.BurnMaskGenerator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class BurnMaskGeneratorModule {
    @Singleton
    @Binds
    abstract fun bindBurnMaskGenerator(impl: BurnMaskGeneratorImpl): BurnMaskGenerator
}