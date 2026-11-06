package com.example.burnmeter.domain.models

data class BurnAssessmentResult(
    val result: Map<Byte, Float>,
) {
    operator fun plus(other: BurnAssessmentResult): BurnAssessmentResult {
        val newResult = mutableMapOf<Byte, Float>()
        newResult.putAll(result)

        for ((key, value) in other.result) {
            newResult[key] = (newResult.getOrDefault(key, 0f) + value)
        }
        return BurnAssessmentResult(newResult)
    }
}


