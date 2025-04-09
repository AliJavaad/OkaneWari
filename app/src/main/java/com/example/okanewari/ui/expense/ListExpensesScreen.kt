package com.example.okanewari.ui.expense

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.data.GetDummyExpenses
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.DisplayFab

object ListExpensesDestination : NavigationDestination {
    override val route = "list_expenses"
    override val titleRes = R.string.list_expenses
    const val partyIdArg = "partyIdArg"
    val routeWithArg = "{$route}/{$partyIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListExpensesScreen(
    onAddExpenseButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListExpensesViewModel = viewModel(factory = OwViewModelProvider.Factory)
){
    // TODO get from database
    val myParty = GetDummyExpenses()


    Scaffold (
        topBar = {
            OkaneWareTopAppBar(
                // TODO title should be the current party name
                title = viewModel.partyUiState.partyDetails.partyName,
                canNavigateBack = true,
                navigateUp = navigateUp,
                actionButtons = {
                    IconButton(onClick = onSettingsButtonClicked ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.settings_buttton_description)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            DisplayFab(
                myClick = onAddExpenseButtonClicked
            )
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = modifier.padding(innerPadding)
        ){
            // TODO case where expenses are empty
            items(myParty.expenseList){
                Card (
                    // TODO make each card clickable
                    modifier = Modifier.fillMaxSize().padding(12.dp)
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            // TODO align text in the column
                            text = it.expenseName,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            // TODO align text in the column
                            text = myParty.moneySymbol + it.total,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
