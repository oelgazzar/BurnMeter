package com.example.burnmeter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.burnmeter.ui.BodySurfaceAreaDrawingCanvas
import com.example.burnmeter.ui.DrawingViewModel
import com.example.burnmeter.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val viewModel: DrawingViewModel = viewModel()
    Column {
        BodySurfaceAreaDrawingCanvas(
            image = R.drawable.back,
            drawingState = viewModel.drawingState,
            modifier = Modifier.size(800.dp)
        )
    }
}
