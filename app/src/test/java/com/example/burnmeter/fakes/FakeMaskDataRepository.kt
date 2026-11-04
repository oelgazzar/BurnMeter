package com.example.burnmeter.fakes

import com.example.burnmeter.domain.models.BodyView
import com.example.burnmeter.domain.models.MaskData
import com.example.burnmeter.domain.repository.MaskDataRepository

class FakeMaskDataRepository(
    private val maskData: MaskData
) : MaskDataRepository {

    override suspend fun getMaskData(bodyView: BodyView): MaskData = maskData
}