package com.example.burnmeter.utils

import kotlin.math.round


fun Double.roundToNearestHalf(): Double {
    return round(this * 2) / 2
}

fun Float.roundToNearestHalf(): Float {
    return round(this * 2) / 2
}