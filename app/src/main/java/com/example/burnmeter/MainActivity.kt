package com.example.burnmeter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.burnmeter.domain.models.BodyView
import com.example.burnmeter.domain.models.getRegionById
import com.example.burnmeter.ui.BodySurfaceAreaDrawingCanvas
import com.example.burnmeter.ui.BodyViewSelector
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
    val strokes by viewModel.strokes.collectAsStateWithLifecycle()
    val bodyView by viewModel.currentBodyView.collectAsStateWithLifecycle()
    val imageRes = when (bodyView) {
        BodyView.FRONT -> R.drawable.front
        BodyView.BACK -> R.drawable.back
    }
    val assessmentResult by viewModel.assessmentResult.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.safeDrawingPadding()
    ) {
        BodyViewSelector(
            selectedView = bodyView,
            onBodyViewSelected = viewModel::updateBodyView,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row() {
            BodySurfaceAreaDrawingCanvas(
                image = imageRes,
                strokes = strokes,
                onAddStroke = viewModel::onAddStroke,
                onBitmapLoaded = viewModel::onBitmapLoaded,
                modifier = Modifier
                    .border(1.dp, Color.Red)
                    .weight(1f)
                    .fillMaxHeight()
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column() {
                assessmentResult.result.forEach { (regionId, fraction) ->
                    Text(getRegionById(regionId).displayText + ": %.2f".format(fraction))
                }
            }
        }
    }
}
