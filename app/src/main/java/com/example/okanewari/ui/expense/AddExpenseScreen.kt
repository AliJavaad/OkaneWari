package com.example.okanewari.ui.expense

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.components.DoneAndCancelButtons

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
        Column(
            modifier = Modifier.padding(innerPadding)
        )
        {
            DoneAndCancelButtons(
                doneButtonClick = navigateBack,
                cancelButtonClick = navigateBack
            )
        }
    }
}

