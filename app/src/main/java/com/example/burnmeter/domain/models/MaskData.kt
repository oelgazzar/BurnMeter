package com.example.burnmeter.domain.models

data class MaskData(
    val regionIds: ByteArray,   // -1 is no region
    val width: Int,
    val height: Int,
    val regionPixelCount: Map<Byte, Int>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MaskData

        if (width != other.width) return false
        if (height != other.height) return false
        if (!regionIds.contentEquals(other.regionIds)) return false
        if (regionPixelCount != other.regionPixelCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + regionIds.contentHashCode()
        result = 31 * result + regionPixelCount.hashCode()
        return result
    }
}