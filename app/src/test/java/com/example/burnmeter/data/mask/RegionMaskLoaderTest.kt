package com.example.burnmeter.data.mask

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.burnmeter.domain.models.MaskPalette
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
class RegionMaskLoaderTest {
    @Test
    fun `toRegionIds converts pixels to region ids`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val loader = RegionMaskLoader(context)
        val pixels = intArrayOf(1, 2, 3, 4)
        val palette = MaskPalette(mapOf(1 to 2, 2 to 2, 3 to -1, 4 to 2))

        val result = loader.toRegionIds(pixels, palette)
        assertArrayEquals(byteArrayOf(2, 2, -1, 2), result)
    }
}