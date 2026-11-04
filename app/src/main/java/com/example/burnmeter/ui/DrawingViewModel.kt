package com.example.burnmeter.ui

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burnmeter.domain.analyzer.BurnFractionAnalyzer
import com.example.burnmeter.domain.models.BodyView
import com.example.burnmeter.domain.models.DrawingPoint
import com.example.burnmeter.domain.models.DrawingStroke
import com.example.burnmeter.domain.models.Region
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val burnFractionAnalyzer: BurnFractionAnalyzer
) : ViewModel() {
    private val _strokes = MutableStateFlow<List<DrawingStroke>>(emptyList())
    private val _activeStrokePoints = MutableStateFlow<List<DrawingPoint>>(emptyList())
    val strokes = combine(_strokes, _activeStrokePoints) { strokes, activeStrokePoints ->
        strokes + DrawingStroke(activeStrokePoints)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _currentBodyView = MutableStateFlow<BodyView>(BodyView.FRONT)
    val currentBodyView = _currentBodyView.asStateFlow()

    private var bitmapWidth: Int? = null
    private var bitmapHeight: Int? = null

    fun onBitmapLoaded(width: Int, height: Int) {
        bitmapWidth = width
        bitmapHeight = height
    }

    fun updateBodyView(bodyView: BodyView) {
        _currentBodyView.value = bodyView
    }

    fun onStartStroke(coordinates: Offset) {
        _activeStrokePoints.value = listOf(DrawingPoint(coordinates))
    }

    fun onExtendStroke(coordinates: Offset) {
        _activeStrokePoints.update {
            it + DrawingPoint(coordinates)
        }
    }

    fun onEndStroke() {
        if (_activeStrokePoints.value.isNotEmpty()) {
            _strokes.update {
                it + DrawingStroke(_activeStrokePoints.value)
            }
        }
        _activeStrokePoints.value = emptyList()
        calculateBurnFraction()
    }

    fun calculateBurnFraction() {
        val width= bitmapWidth?: return
        val height = bitmapHeight?: return

        viewModelScope.launch {
            try {
                val result = burnFractionAnalyzer.calculateBurnFractionPerRegion(
                    strokes.value,
                    width,
                    height,
                    _currentBodyView.value
                )
                println(result.map { Region.entries.first { region -> region.id == it.key }.name to it.value })
            } catch (_: Exception) {

            }
        }
    }
}