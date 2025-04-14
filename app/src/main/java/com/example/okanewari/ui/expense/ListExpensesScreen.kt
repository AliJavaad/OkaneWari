package com.example.okanewari.ui.expense

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.Expense
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.data.GetDummyExpenses
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.DisplayFab
import com.example.okanewari.ui.party.PartyDetails

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
    onSettingsButtonClicked: (Int) -> Unit,
    onExpenseCardClick: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListExpensesViewModel = viewModel(factory = OwViewModelProvider.Factory)
){
    val partyUiState by viewModel.partyUiState.collectAsState()

    // TODO get from database
    val myParty = GetDummyExpenses()

    Scaffold (
        topBar = {
            OkaneWareTopAppBar(
                // TODO title should be the current party name
                title = partyUiState.partyDetails.partyName,
                canNavigateBack = true,
                navigateUp = navigateUp,
                actionButtons = {
                    IconButton(
                        onClick = {onSettingsButtonClicked(partyUiState.partyDetails.id)}
                    ){
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
        ListExpensesBody(
            // expenseList = listOf(),
            expenseList = myParty.expenseList,
            expenseClicked = onExpenseCardClick,
            partyDetails = PartyDetails(2, "TODO"),
            contentPadding = innerPadding
        )
    }
}

@Composable
fun ListExpensesBody(
    expenseList: List<Expense>,
    expenseClicked: () -> Unit,
    partyDetails: PartyDetails,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    if (expenseList.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxHeight(0.6f)
        ) {
            Text(
                text = stringResource(R.string.empty_expense_list),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding).fillMaxWidth()
            )
        }
    } else {
        DisplayExpenses(
            expenseList = expenseList,
            expenseClicked = expenseClicked,
            partyDetails = partyDetails,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun DisplayExpenses(
    expenseList: List<Expense>,
    expenseClicked: () -> Unit,
    partyDetails: PartyDetails,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = Modifier.padding(contentPadding)
    ) {
        items(expenseList) {
            Card(
                // TODO make each card clickable
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .clickable { expenseClicked() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                        text = partyDetails.currency + it.total,
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