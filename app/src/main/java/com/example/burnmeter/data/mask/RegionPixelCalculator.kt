package com.example.burnmeter.data.mask

fun calculateRegionPixelCount(regionIds: ByteArray): Map<Byte, Int> {
    val pixelCountPerRegionId = mutableMapOf<Byte, Int>()
    for (regionId in regionIds) {
        pixelCountPerRegionId[regionId] = pixelCountPerRegionId.getOrDefault(regionId, 0) + 1
    }
    return pixelCountPerRegionId
}