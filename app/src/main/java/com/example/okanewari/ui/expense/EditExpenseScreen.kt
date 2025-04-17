package com.example.okanewari.ui.expense

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.components.DoneAndCancelButtons

object EditExpenseDestination: NavigationDestination{
    override val route = "edit_expense"
    override val titleRes = R.string.edit_expense
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExpenseScreen(
    navigateUp: () -> Unit,
    navigateBack: () -> Unit
){
    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = stringResource(EditExpenseDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ){ innerPadding ->
        EditExpenseBody(
            onDone = navigateBack,
            onCancel = navigateBack,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun EditExpenseBody(
    onDone: () -> Unit,
    onCancel: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = Modifier.padding(contentPadding)
    ) {

        DoneAndCancelButtons(
            doneButtonClick = onDone,
            cancelButtonClick = onCancel
        )
    }
}

