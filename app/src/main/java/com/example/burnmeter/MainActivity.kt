package com.example.burnmeter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.burnmeter.domain.analyzer.calculateTBSA
import com.example.burnmeter.domain.models.BodyView
import com.example.burnmeter.domain.models.DrawingMode
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
    val tbsa = remember { Animatable(0f) }
    val mode by viewModel.mode.collectAsStateWithLifecycle()

    LaunchedEffect(assessmentResult) {
        tbsa.animateTo(calculateTBSA(assessmentResult).toFloat())
    }


    Column(
        modifier = Modifier.safeDrawingPadding()
    ) {
        BodyViewSelector(
            selectedView = bodyView,
            onBodyViewSelected = viewModel::updateBodyView,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.weight(1f)) {
            BodySurfaceAreaDrawingCanvas(
                image = imageRes,
                mode = mode,
                strokes = strokes,
                onAddStroke = viewModel::onAddStroke,
                onDeleteStroke = viewModel::onDeleteStroke,
                onBitmapLoaded = viewModel::onBitmapLoaded,
                modifier = Modifier
                    .weight(1f)
                    .height(500.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text("%.1f %%".format(tbsa.value),
                    style = MaterialTheme.typography.headlineLarge)
                assessmentResult.forEach { (regionId, percentage) ->
                    Row {
                        Text(text = getRegionById(regionId).displayText)
//                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "  %.1f%%".format(percentage))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row() {
            Button(onClick = { viewModel.updateMode(DrawingMode.Paint) }) {
                Text("Paint")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { viewModel.updateMode(DrawingMode.Erase) }) {
                Text("Erase")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(mode.name)
        }
    }
}
