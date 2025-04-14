package com.example.okanewari.ui.expense

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.components.DoneAndCancelButtons
import com.example.okanewari.ui.party.PartyDetails
import com.example.okanewari.ui.party.PartyUiState

object AddExpenseDestination: NavigationDestination{
    override val route = "add_new_expense"
    override val titleRes = R.string.add_new_expense
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    navigateUp: () -> Unit,
    navigateBack: () -> Unit
){
    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = stringResource(AddExpenseDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        }
    ){ innerPadding ->
        ExpenseEntryBody(
            onDone = navigateBack,
            onCancel = navigateBack,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun ExpenseEntryBody(
    onDone: () -> Unit,
    onCancel: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = Modifier.padding(contentPadding)
    ) {
        ExpenseInputForm(

        )
        DoneAndCancelButtons(
            doneButtonClick = onDone,
            cancelButtonClick = onCancel
        )
    }
}

@Composable
fun ExpenseInputForm() {
    // TODO
}

