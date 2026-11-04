package com.example.burnmeter.data.mask

import android.graphics.Canvas
import androidx.core.graphics.createBitmap
import com.example.burnmeter.domain.mask.BurnMaskGenerator
import com.example.burnmeter.domain.models.DrawingStroke
import com.example.burnmeter.drawing.CanvasRenderer
import com.example.burnmeter.drawing.DrawingPaintFactory
import javax.inject.Inject

class BurnMaskGeneratorImpl @Inject constructor(
    paintFactory: DrawingPaintFactory,
    private val renderer: CanvasRenderer
) : BurnMaskGenerator {
    val paint = paintFactory.createStrokePaint()

    override fun generateBurnMask(
        strokes: List<DrawingStroke>,
        width: Int,
        height: Int,
    ): IntArray {
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        renderer.drawStrokes(canvas, strokes, paint)
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        return pixels
    }
}