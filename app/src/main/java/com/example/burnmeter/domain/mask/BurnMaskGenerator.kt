package com.example.burnmeter.domain.mask

import com.example.burnmeter.domain.models.DrawingStroke

interface BurnMaskGenerator {
    fun generateBurnMask(
        strokes: List<DrawingStroke>,
        width: Int,
        height: Int,
    ): IntArray
}