package com.example.burnmeter.domain.analyzer

import com.example.burnmeter.domain.models.RegionBurnFraction
import com.example.burnmeter.domain.models.getRegionById
import com.example.burnmeter.utils.roundToNearestHalf
import kotlin.math.roundToInt

fun getRegionBurnPercentages(regionBurnFractions: List<RegionBurnFraction>): Map<Byte, Double> {
    return regionBurnFractions
        .groupBy { it.regionId }
        .map { (regionId, fractions) ->
            val total = fractions.sumOf { it.fraction.toDouble() }
            val region = getRegionById(regionId)
            regionId to (total / region.surfaces * region.percentage ).roundToNearestHalf()
        }
        .filter { it.second > 0.0 }
        .toMap()
}

fun List<RegionBurnFraction>.toRegionBurnPercentages() = getRegionBurnPercentages(this)

fun calculateTBSA(regionBurnFractions: Map<Byte, Double>): Double {
    return regionBurnFractions.entries.fold(0.0) { acc, (_, percentage) ->
        acc + percentage
    }
}