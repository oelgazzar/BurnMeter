package com.example.burnmeter.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.example.burnmeter.domain.models.DrawingPoint
import com.example.burnmeter.domain.models.DrawingStroke
import kotlin.collections.plus

class DrawingState {
    private val _strokes = mutableStateListOf<DrawingStroke>()
    val activeStrokePoints = mutableStateListOf<DrawingPoint>()
    val strokes: List<DrawingStroke> get() = _strokes + DrawingStroke(activeStrokePoints)

    fun startStroke(startPoint: DrawingPoint) {
        _strokes.add(DrawingStroke(listOf(startPoint)))
        activeStrokePoints.add(startPoint)
    }

    fun extendStroke(point: DrawingPoint) {
        activeStrokePoints.add(point)
    }

    fun endStroke() {
        _strokes.add(DrawingStroke(activeStrokePoints.toList()))
        activeStrokePoints.clear()
    }
}

@Composable
fun rememberDrawingState(): DrawingState {
    return remember { DrawingState() }
}