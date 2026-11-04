package com.example.burnmeter.domain.repository

import com.example.burnmeter.domain.models.BodyView
import com.example.burnmeter.domain.models.MaskData

interface MaskDataRepository {
    suspend fun getMaskData(bodyView: BodyView): MaskData
}