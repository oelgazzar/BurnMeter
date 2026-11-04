package com.example.burnmeter.data.mask

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.burnmeter.domain.models.MaskPalette
import com.example.burnmeter.domain.models.maskPalette
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
        val palette = maskPalette(
            Color.Blue.toArgb(),
            Color.Red.toArgb(),
            Color.Green.toArgb(),
            Color.Magenta.toArgb(),
            Color.Cyan.toArgb(),
            0xFFF0F0F0.toInt(),
            Color.Yellow.toArgb(),
        )
        val loader = RegionMaskLoader(context, palette)
        val pixels = intArrayOf(Color.Blue.toArgb(), Color.Green.toArgb(), Color.Cyan.toArgb(), Color.Yellow.toArgb())

        val result = loader.toRegionIds(pixels)
        assertArrayEquals(byteArrayOf(1, 3, 5, 8), result)
    }
}