package com.example.burnmeter.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

data class CanvasTransform(
    val scale: Float,
    val offset: Offset
) {
    fun toImageCoordinates(position: Offset): Offset {
        return Offset(
            x = (position.x - offset.x) / scale,
            y = (position.y - offset.y) / scale
        )
    }
}

fun calculateCanvasTransform(
    canvasSize: Size,
    imageSize: Size
): CanvasTransform {
    val scale = minOf(
        canvasSize.width / imageSize.width,
        canvasSize.height / imageSize.height
    )

    val offset = Offset(
        x = (canvasSize.width - imageSize.width * scale) / 2f,
        y = 0f
//        y = (canvasSize.height - imageSize.height * scale) / 2f
    )

    return CanvasTransform(
        scale = scale,
        offset = offset
    )
}

fun calculateImageCoordinates(
    canvasSize: Size,
    imageSize: Size,
    position: Offset
) = calculateCanvasTransform(canvasSize, imageSize).toImageCoordinates(position)