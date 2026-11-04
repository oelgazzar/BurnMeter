package com.example.burnmeter.domain.models

data class MaskPalette(
    val colorMap: Map<Int, Byte>,
)

fun maskPalette(
    headAndNeck: Int,
    rightArm: Int,
    leftArm: Int,
    rightLeg: Int,
    leftLeg: Int,
    trunk: Int,
    perineum: Int,
): MaskPalette {
    return MaskPalette(
        mapOf(
            headAndNeck to Region.HEAD_AND_NECK.id,
            rightArm to Region.RIGHT_ARM.id,
            leftArm to Region.LEFT_ARM.id,
            rightLeg to Region.RIGHT_LEG.id,
            leftLeg to Region.LEFT_LEG.id,
            trunk to Region.ANTERIOR_TRUNK.id,
            perineum to Region.PERINEUM.id,
        )
    )
}
