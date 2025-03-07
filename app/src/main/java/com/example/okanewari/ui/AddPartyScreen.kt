package com.example.okanewari.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.okanewari.ui.components.DoneAndCancelButtons


@Composable
fun AddPartyScreen(
    onDoneButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    DoneAndCancelButtons(
        doneButtonClick = onDoneButtonClicked,
        cancelButtonClick = onCancelButtonClicked
    )
}