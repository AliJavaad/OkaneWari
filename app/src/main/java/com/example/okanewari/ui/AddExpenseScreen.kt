package com.example.okanewari.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.components.DoneAndCancelButtons

object AddExpenseDestination: NavigationDestination{
    override val route = "add_new_expense"
    override val titleRes = R.string.add_new_expense
}

@Composable
fun AddExpenseScreen(
    onDoneButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    DoneAndCancelButtons(
        doneButtonClick = onDoneButtonClicked,
        cancelButtonClick = onCancelButtonClicked
    )
}

