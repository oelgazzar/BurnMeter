package com.example.burnmeter.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrawingViewModel @Inject constructor(): ViewModel() {
    val drawingState = DrawingState()
}