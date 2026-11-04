package com.example.burnmeter.fakes

import com.example.burnmeter.domain.mask.BurnMaskGenerator
import com.example.burnmeter.domain.models.DrawingStroke

class FakeBurnMaskGenerator(
    private val burnMask: IntArray
) : BurnMaskGenerator {

    override fun generateBurnMask(
        strokes: List<DrawingStroke>,
        width: Int,
        height: Int
    ): IntArray = burnMask
}