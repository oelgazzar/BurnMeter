package com.example.burnmeter.domain.analyzer

import com.example.burnmeter.domain.mask.BurnMaskGenerator
import com.example.burnmeter.domain.models.BodyView
import com.example.burnmeter.domain.models.BurnAssessmentResult
import com.example.burnmeter.domain.models.RegionBurnFraction
import com.example.burnmeter.domain.models.DrawingStroke
import com.example.burnmeter.domain.repository.MaskDataRepository
import javax.inject.Inject

class BurnFractionAnalyzer @Inject constructor(
    private val maskDataRepository: MaskDataRepository,
    private val burnMaskGenerator: BurnMaskGenerator
) {
    suspend fun calculateBurnFractionPerRegion(
        strokes: List<DrawingStroke>,
        width: Int,
        height: Int,
        bodyView: BodyView
    ): BurnAssessmentResult {
        val maskData = maskDataRepository.getMaskData(bodyView)
        val burnMask = burnMaskGenerator.generateBurnMask(strokes, width, height)
        require(maskData.width == width && maskData.height == height) {
            "Burn mask and region mask dimensions must match"
        }
        val regionIds = maskData.regionIds
        val regionPixelCount = maskData.regionPixelCount

        val regionBurntPixels = mutableMapOf<Byte, Int>()

        for (index in burnMask.indices) {
            val pixel = burnMask[index]
            val regionId = regionIds[index]
            if (regionId.toInt() == -1) continue
            if (pixel == 0) continue
            regionBurntPixels[regionId] = regionBurntPixels.getOrDefault(regionId, 0) + 1
        }

        return BurnAssessmentResult(
            result = regionBurntPixels.map {
                RegionBurnFraction(
                    regionId = it.key,
                    bodyView = bodyView,
                    fraction = it.value.toFloat() / regionPixelCount.getValue(it.key)
                )
            }
        )
    }
}