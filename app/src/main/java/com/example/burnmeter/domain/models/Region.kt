package com.example.burnmeter.domain.models

import com.example.burnmeter.domain.models.Region.ANTERIOR_TRUNK
import com.example.burnmeter.domain.models.Region.HEAD_AND_NECK
import com.example.burnmeter.domain.models.Region.LEFT_ARM
import com.example.burnmeter.domain.models.Region.LEFT_LEG
import com.example.burnmeter.domain.models.Region.PERINEUM
import com.example.burnmeter.domain.models.Region.POSTERIOR_TRUNK
import com.example.burnmeter.domain.models.Region.RIGHT_ARM
import com.example.burnmeter.domain.models.Region.RIGHT_LEG

enum class Region(val id: Byte, val displayText: String, val percentage: Int, val surfaces: Int) {
    HEAD_AND_NECK(1, "Head & Neck", 9, 2),
    RIGHT_ARM(2, "Right Arm", 9, 2),
    LEFT_ARM(3, "Left Arm", 9, 2),
    RIGHT_LEG(4, "Right Leg", 18, 2),
    LEFT_LEG(5, "Left Leg", 18, 2),
    ANTERIOR_TRUNK(6, "Anterior Trunk", 18, 1),
    POSTERIOR_TRUNK(7, "Posterior Trunk", 18, 1),
    PERINEUM(8, "Perineum", 1, 1)
}

fun getRegionById(id: Byte): Region {
    return when(id) {
        1.toByte() -> HEAD_AND_NECK
        2.toByte() -> RIGHT_ARM
        3.toByte() -> LEFT_ARM
        4.toByte() -> RIGHT_LEG
        5.toByte() -> LEFT_LEG
        6.toByte() -> ANTERIOR_TRUNK
        7.toByte() -> POSTERIOR_TRUNK
        else -> PERINEUM
    }
}