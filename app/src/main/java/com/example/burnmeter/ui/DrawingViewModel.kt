package com.example.burnmeter.ui

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burnmeter.domain.analyzer.BurnFractionAnalyzer
import com.example.burnmeter.domain.analyzer.calculateTBSA
import com.example.burnmeter.domain.analyzer.getRegionBurnPercentages
import com.example.burnmeter.domain.analyzer.toRegionBurnPercentages
import com.example.burnmeter.domain.models.BodyView
import com.example.burnmeter.domain.models.BurnAssessmentResult
import com.example.burnmeter.domain.models.DrawingMode
import com.example.burnmeter.domain.models.DrawingStroke
import com.example.burnmeter.drawing.findStrokeToErase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyMap

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val burnFractionAnalyzer: BurnFractionAnalyzer
) : ViewModel() {
    private var bitmapWidth: Int? = null
    private var bitmapHeight: Int? = null

    private val _currentBodyView = MutableStateFlow(BodyView.FRONT)
    val currentBodyView = _currentBodyView.asStateFlow()

    private val _frontStrokes = MutableStateFlow<List<DrawingStroke>>(emptyList())
    private val _backStrokes = MutableStateFlow<List<DrawingStroke>>(emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val strokes = _currentBodyView.flatMapLatest {
        when (it) {
            BodyView.FRONT -> _frontStrokes
            BodyView.BACK -> _backStrokes
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _mode = MutableStateFlow(DrawingMode.Paint)
    val mode = _mode.asStateFlow()

    private val _assessmentResult = MutableStateFlow<Map<BodyView, BurnAssessmentResult>>(emptyMap())
    val assessmentResult = _assessmentResult.map { groupedResults ->
        groupedResults.values.flatMap { it.result }.toRegionBurnPercentages()
    }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyMap()
        )


    fun onBitmapLoaded(width: Int, height: Int) {
        bitmapWidth = width
        bitmapHeight = height
    }

    fun updateBodyView(bodyView: BodyView) {
        _currentBodyView.value = bodyView
    }

    fun onAddStroke(newStroke: DrawingStroke) {
        val currentStrokes = when (_currentBodyView.value) {
            BodyView.FRONT -> _frontStrokes
            BodyView.BACK -> _backStrokes
        }
        currentStrokes.update {
            it + newStroke
        }
        calculateBurnFraction()
    }

    fun calculateBurnFraction() {
        val width = bitmapWidth ?: return
        val height = bitmapHeight ?: return

        viewModelScope.launch {
            try {
                val result = burnFractionAnalyzer.calculateBurnFractionPerRegion(
                    strokes.value,
                    width,
                    height,
                    _currentBodyView.value
                )
                _assessmentResult.update {
                    it + (_currentBodyView.value to result)
                }
                println(assessmentResult.value)
                println(calculateTBSA(assessmentResult.value))
            } catch (e: Exception) {
                Log.e("DrawingViewModel", "Error calculating burn fraction $e")
            }
        }
    }

    fun onDeleteStroke(position: Offset) {
        val strokeIndex = findStrokeToErase(position, strokes.value, 10f)
        if (strokeIndex != null) {
            val updatedStrokes = strokes.value.toMutableList()
            updatedStrokes.removeAt(strokeIndex)
            when (_currentBodyView.value) {
                BodyView.FRONT -> _frontStrokes.value = updatedStrokes
                BodyView.BACK -> _backStrokes.value = updatedStrokes
            }
            calculateBurnFraction()
        }
    }

    fun updateMode(mode: DrawingMode) {
        _mode.value = mode
    }
}