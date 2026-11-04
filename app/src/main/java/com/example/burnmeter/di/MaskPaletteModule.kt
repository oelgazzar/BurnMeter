package com.example.burnmeter.di

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.burnmeter.domain.models.MaskPalette
import com.example.burnmeter.domain.models.maskPalette
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaletteModule {

    @Provides
    @Singleton
    fun provideMaskPalette() = maskPalette(
        Color.Blue.toArgb(),
        Color.Red.toArgb(),
        Color.Green.toArgb(),
        Color.Magenta.toArgb(),
        Color.Cyan.toArgb(),
        0xFFF0F0F0.toInt(),
        Color.Yellow.toArgb(),
    )
}