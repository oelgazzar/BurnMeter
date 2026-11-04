package com.example.burnmeter.data.mask

import android.content.Context
import android.graphics.BitmapFactory
import androidx.annotation.VisibleForTesting
import com.example.burnmeter.di.IoDispatcher
import com.example.burnmeter.domain.models.BodyView
import com.example.burnmeter.domain.models.MaskPalette
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val BACK_MASK_FILENAME = "masks/back_mask.png"
private const val FRONT_MASK_FILENAME = "masks/front_mask.png"


class RegionMaskLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val palette: MaskPalette,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun decodeMask(bodyView: BodyView): Triple<ByteArray, Int, Int> {
        val filename = when (bodyView) {
            BodyView.FRONT -> FRONT_MASK_FILENAME
            BodyView.BACK -> BACK_MASK_FILENAME
        }
        val bitmap = withContext(dispatcher) {
            context.assets.open(filename).use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        }

        checkNotNull(bitmap)

        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        return Triple(toRegionIds(pixels), bitmap.width, bitmap.height)
    }

    @VisibleForTesting
    fun toRegionIds(pixels: IntArray): ByteArray {
        val regionIds = ByteArray(pixels.size)
        for (i in pixels.indices) {
            regionIds[i] = palette.colorMap.getOrDefault(pixels[i], -1)
        }
        return regionIds
    }
}