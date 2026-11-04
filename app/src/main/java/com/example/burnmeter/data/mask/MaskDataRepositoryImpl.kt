package com.example.burnmeter.data.mask

import com.example.burnmeter.domain.models.BodyView
import com.example.burnmeter.domain.models.MaskData
import com.example.burnmeter.domain.repository.MaskDataRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.system.measureTimeMillis

class MaskDataRepositoryImpl @Inject constructor(
    private val regionMaskLoader: RegionMaskLoader
) : MaskDataRepository {
    private val maskData = mutableMapOf<BodyView, MaskData>()
    private val mutex = Mutex()

    override suspend fun getMaskData(bodyView: BodyView): MaskData {

        return mutex.withLock {
            val time = measureTimeMillis {
                maskData[bodyView]?.let { return it }

                val (regionIds, width, height) = regionMaskLoader.decodeMask(bodyView)
                val regionPixelCount = calculateRegionPixelCount(regionIds)

                MaskData(
                    regionIds = regionIds,
                    width = width,
                    height = height,
                    regionPixelCount = regionPixelCount
                ).also {
                    maskData[bodyView] = it
                }
            }
            println(time)
            maskData.getValue(bodyView)
        }
    }
}
