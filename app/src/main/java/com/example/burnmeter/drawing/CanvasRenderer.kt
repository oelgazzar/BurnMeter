package com.example.burnmeter.drawing

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import com.example.burnmeter.domain.models.DrawingStroke
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CanvasRenderer @Inject constructor(
    private val paintFactory: DrawingPaintFactory
) {
    private val paint = paintFactory.createStrokePaint()
    fun drawStrokes(canvas: Canvas, strokes: List<DrawingStroke>, paint: Paint) {
        val combinedPath = Path()
        for (stroke in strokes) {
            if (stroke.points.isEmpty()) continue
            val path = Path()
            path.moveTo(stroke.points[0].x, stroke.points[0].y)
            for (point in stroke.points.drop(1)) {
                path.lineTo(point.x, point.y)
            }
            combinedPath.addPath(path)
        }
        canvas.drawPath(combinedPath, paint)
    }


    fun DrawScope.drawStrokes(strokes: List<DrawingStroke>) {
        drawStrokes(this.drawContext.canvas.nativeCanvas, strokes, paint = paint)
    }
}
