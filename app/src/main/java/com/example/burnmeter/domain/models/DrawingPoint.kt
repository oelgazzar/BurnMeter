package com.example.burnmeter.domain.models

import androidx.compose.ui.geometry.Offset

data class DrawingPoint(
    val x: Float,
    val y: Float
) {
    constructor(position: Offset) : this(position.x, position.y)
}