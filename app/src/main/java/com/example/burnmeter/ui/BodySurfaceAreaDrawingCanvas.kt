package com.example.burnmeter.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.toSize
import com.example.burnmeter.domain.models.BodyView
import com.example.burnmeter.domain.models.DrawingStroke
import com.example.burnmeter.drawing.CanvasRenderer
import com.example.burnmeter.drawing.DrawingPaintFactory
import com.example.burnmeter.drawing.calculateCanvasTransform
import com.example.burnmeter.drawing.calculateImageCoordinates

@Composable
fun BodySurfaceAreaDrawingCanvas(
    @DrawableRes image: Int,
    strokes: List<DrawingStroke>,
    onAddStroke: (Offset) -> Unit,
    onExtendStroke: (Offset) -> Unit,
    onEndStroke: () -> Unit,
    onBitmapLoaded: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val renderer = remember { CanvasRenderer(DrawingPaintFactory()) }
    val imageBitmap = ImageBitmap.imageResource(image)

    LaunchedEffect(imageBitmap) {
        onBitmapLoaded(imageBitmap.width, imageBitmap.height)
    }

    Canvas(
        modifier = modifier
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        val imageCoordinates = calculateImageCoordinates(
                            size.toSize(),
                            Size(imageBitmap.width.toFloat(), imageBitmap.height.toFloat()),
                            it
                        )
                        onAddStroke(imageCoordinates)
                    },
                    onDrag = { change, _ ->
                        val imageCoordinates = calculateImageCoordinates(
                            size.toSize(),
                            Size(imageBitmap.width.toFloat(), imageBitmap.height.toFloat()),
                            change.position
                        )
                        onExtendStroke(imageCoordinates)
                    },
                    onDragEnd = {
                        onEndStroke()
                    }
                )
            }
    ) {
        val transform = calculateCanvasTransform(
            size, Size(
                imageBitmap.width.toFloat(),
                imageBitmap.height.toFloat()
            )
        )
        translate(transform.offset.x, transform.offset.y) {
            scale(transform.scale, transform.scale, pivot = Offset.Zero) {
                clipRect(0f, 0f, imageBitmap.width.toFloat(), imageBitmap.height.toFloat()) {
                    drawImage(imageBitmap)
                    with(renderer) {
                        drawStrokes(strokes)
                    }
                    drawImage(
                        imageBitmap,
                        blendMode = BlendMode.DstIn
                    )
                }
            }
        }
    }
}