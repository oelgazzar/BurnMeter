package com.example.burnmeter.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.burnmeter.ui.theme.AppTheme


@Composable
fun AnimatedSegmentedControl(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
    animationDuration: Int = 250,
) {
    BoxWithConstraints(
        modifier = modifier
            .height(height)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .selectableGroup()
    ) {
        val itemWidth = maxWidth / options.size
        val selectedIndex = options.indexOf(selectedOption)

        val indicatorOffset by animateDpAsState(
            targetValue = itemWidth * selectedIndex,
            animationSpec = tween(
                durationMillis = animationDuration,
            ),
            label = "indicatorOffset"
        )

        // Sliding indicator
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(indicatorOffset.roundToPx(), 0)
                }
                .width(itemWidth)
                .fillMaxHeight()
                .padding(4.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )

        // Options
        Row(modifier = Modifier.fillMaxSize()) {
            options.forEach { option ->
                val selected = option == selectedOption
                val textColor by animateColorAsState(
                    targetValue = if (selected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    animationSpec = tween(
                        animationDuration
                    )
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .selectable(
                            selected = selected,
                            onClick = { onOptionSelected(option) },
                            indication = null,
                            interactionSource = null,
                            role = Role.Tab
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        color = textColor,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AnimatedSegmentedControlPrev() {
    var selectedOption by remember { mutableStateOf("Front") }
    AppTheme {
        AnimatedSegmentedControl(
            options = listOf("Front", "Back"),
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it },
            modifier = Modifier.safeDrawingPadding()
        )
    }
}
