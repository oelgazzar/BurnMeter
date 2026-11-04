package com.example.burnmeter.domain.models

enum class Region(val id: Byte, val displayText: String) {
    HEAD_AND_NECK(1, "Head & Neck"),
    RIGHT_ARM(2, "Right Arm"),
    LEFT_ARM(3, "Left Arm"),
    RIGHT_LEG(4, "Right Leg"),
    LEFT_LEG(5, "Left Leg"),
    ANTERIOR_TRUNK(6, "Anterior Trunk"),
    POSTERIOR_TRUNK(7, "Posterior Trunk"),
    PERINEUM(8, "Perineum"),
}