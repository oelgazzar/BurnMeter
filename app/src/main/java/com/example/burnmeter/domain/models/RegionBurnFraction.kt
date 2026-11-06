package com.example.burnmeter.domain.models

data class RegionBurnFraction(
    val regionId: Byte,
    val bodyView: BodyView,
    val fraction: Float,
)


