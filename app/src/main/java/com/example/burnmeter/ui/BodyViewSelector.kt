package com.example.burnmeter.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.burnmeter.domain.models.BodyView
import com.example.burnmeter.ui.components.AnimatedSegmentedControl

@Composable
fun BodyViewSelector(
    onBodyViewSelected: (BodyView) -> Unit,
    modifier: Modifier = Modifier,
    selectedView : BodyView = BodyView.FRONT,
) {
    AnimatedSegmentedControl(
        options = BodyView.entries.map { it.displayName },
        selectedOption = selectedView.displayName,
        onOptionSelected = { onBodyViewSelected(BodyView.entries.first { view -> view.displayName == it }) },
        modifier = modifier
    )
}