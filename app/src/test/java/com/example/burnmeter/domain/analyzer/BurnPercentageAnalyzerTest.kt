package com.example.burnmeter.domain.analyzer

import com.example.burnmeter.domain.models.BodyView
import com.example.burnmeter.domain.models.MaskData
import com.example.burnmeter.fakes.FakeBurnMaskGenerator
import com.example.burnmeter.fakes.FakeMaskDataRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertFailsWith

class BurnFractionAnalyzerTest {

    private val width = 4
    private val height = 2

    private val region1: Byte = 1
    private val region2: Byte = 2
    private val noRegion: Byte = -1

    private fun createAnalyzer(
        regionIds: ByteArray,
        regionPixelCount: Map<Byte, Int>,
        burnMask: IntArray,
    ): BurnFractionAnalyzer {

        val maskData = MaskData(
            regionIds = regionIds,
            width = width,
            height = height,
            regionPixelCount = regionPixelCount
        )

        return BurnFractionAnalyzer(
            maskDataRepository = FakeMaskDataRepository(maskData),
            burnMaskGenerator = FakeBurnMaskGenerator(burnMask)
        )
    }

    @Test
    fun `calculates burn fraction for each region`() = runTest {
        // Arrange
        val regionIds = byteArrayOf(
            1, 1, 1, 1,
            2, 2, 2, 2
        )

        val burnMask = intArrayOf(
            1, 1, 0, 0,
            1, 0, 0, 0
        )

        val analyzer = createAnalyzer(
            regionIds = regionIds,
            regionPixelCount = mapOf(
                region1 to 4,
                region2 to 4
            ),
            burnMask = burnMask
        )

        // Act
        val result = analyzer.calculateBurnFractionPerRegion(
            strokes = emptyList(),
            width = width,
            height = height,
            bodyView = BodyView.FRONT
        )

        // Assert
        assertEquals(0.5f, result[region1])
        assertEquals(0.25f, result[region2])
    }

    @Test
    fun `ignores pixels outside any region`() = runTest {
        // Arrange
        val regionIds = byteArrayOf(
            1, 1, -1, -1,
            2, 2, -1, -1
        )

        val burnMask = intArrayOf(
            1, 1, 1, 1,
            1, 0, 1, 1
        )

        val analyzer = createAnalyzer(
            regionIds = regionIds,
            regionPixelCount = mapOf(
                region1 to 2,
                region2 to 2
            ),
            burnMask = burnMask
        )

        // Act
        val result = analyzer.calculateBurnFractionPerRegion(
            emptyList(),
            width,
            height,
            BodyView.FRONT
        )

        // Assert
        assertEquals(1f, result[region1])
        assertEquals(0.5f, result[region2])
        assertEquals(2, result.size)
    }

    @Test
    fun `returns empty map when there are no burned pixels`() = runTest {
        val analyzer = createAnalyzer(
            regionIds = byteArrayOf(1, 1, 2, 2, 1, 1, 2, 2),
            regionPixelCount = mapOf(
                region1 to 4,
                region2 to 4
            ),
            burnMask = IntArray(8)
        )

        val result = analyzer.calculateBurnFractionPerRegion(
            emptyList(),
            width,
            height,
            BodyView.FRONT
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `returns fraction one when entire region is burned`() = runTest {
        val analyzer = createAnalyzer(
            regionIds = byteArrayOf(1, 1, 1, 1, 2, 2, 2, 2),
            regionPixelCount = mapOf(
                region1 to 4,
                region2 to 4
            ),
            burnMask = intArrayOf(1, 1, 1, 1, 0, 0, 0, 0)
        )

        val result = analyzer.calculateBurnFractionPerRegion(
            emptyList(),
            width,
            height,
            BodyView.FRONT
        )

        assertEquals(1f, result[region1])
        assertFalse(region2 in result)
    }

    @Test
    fun `throws when mask dimensions do not match requested dimensions`() = runTest {
        val analyzer = createAnalyzer(
            regionIds = ByteArray(8),
            regionPixelCount = emptyMap(),
            burnMask = IntArray(8)
        )

        assertFailsWith<IllegalArgumentException> {
                analyzer.calculateBurnFractionPerRegion(
                    emptyList(),
                    width = 2,
                    height = 4,
                    BodyView.FRONT
                )
        }
    }
}