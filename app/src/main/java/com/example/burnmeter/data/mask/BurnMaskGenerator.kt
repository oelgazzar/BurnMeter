package com.example.burnmeter.data.mask

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.graphics.createBitmap
import com.example.burnmeter.domain.models.DrawingStroke
import com.example.burnmeter.drawing.CanvasRenderer
import com.example.burnmeter.drawing.DrawingPaintFactory
import javax.inject.Inject

class BurnMaskGenerator @Inject constructor(
    paintFactory: DrawingPaintFactory,
    private val renderer: CanvasRenderer
) {
    val paint = paintFactory.createStrokePaint()

    fun generateBurnMask(
        strokes: List<DrawingStroke>,
        width: Int,
        height: Int,
    ): Bitmap {
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        renderer.drawStrokes(canvas, strokes, paint)
        return bitmap
    }
}