package com.example.burnmeter.data.mask

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.VisibleForTesting
import com.example.burnmeter.di.IoDispatcher
import com.example.burnmeter.domain.models.MaskPalette
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val MASK_FILENAME = "masks/front_mask.png"


class RegionMaskLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val palette: MaskPalette,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun decodeMask(): Triple<ByteArray, Int, Int> {
        val bitmap = withContext(dispatcher) {
            context.assets.open(MASK_FILENAME).use { stream ->
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