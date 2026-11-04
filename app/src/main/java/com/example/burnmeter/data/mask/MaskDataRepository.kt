package com.example.burnmeter.data.mask

import com.example.burnmeter.domain.models.MaskData
import com.example.burnmeter.domain.models.MaskPalette
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class MaskDataRepository @Inject constructor(
    private val regionMaskLoader: RegionMaskLoader
){
    private var maskData: MaskData? = null
    private val mutex = Mutex()

    suspend fun getMaskData(): MaskData {
        return mutex.withLock {
            maskData?.let { return it }

            val (regionIds, width, height) = regionMaskLoader.decodeMask()
            val regionPixelCount = calculateRegionPixelCount(regionIds)

            MaskData(
                regionIds = regionIds,
                width = width,
                height = height,
                regionPixelCount = regionPixelCount
            ).also {
                maskData = it
            }
        }
    }
}