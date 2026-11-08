package com.example.burnmeter.drawing

import androidx.compose.ui.geometry.Offset
import com.example.burnmeter.domain.models.DrawingStroke
import kotlin.math.pow

fun findStrokeToErase(
    offset: Offset,
    strokes: List<DrawingStroke>,
    eraserRadius: Float
): Int? {
    return strokes
        .mapIndexedNotNull { index, stroke ->
            val minDistanceSquared = stroke.points.minOfOrNull { point ->
                (point - offset).getDistanceSquared()
            } ?: return@mapIndexedNotNull null

            if (minDistanceSquared <= eraserRadius * eraserRadius) {
                index to minDistanceSquared
            } else {
                null
            }
        }
        .minByOrNull { it.second }
        ?.first
}