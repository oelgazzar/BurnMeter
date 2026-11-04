package com.example.burnmeter.drawing

import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class DrawingPaintFactory @Inject constructor() {
    fun createStrokePaint(): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            strokeWidth = 36f
            color = Color.Red.toArgb()
            alpha = 0xff / 2
        }
}