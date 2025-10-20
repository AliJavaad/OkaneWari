package com.javs.okanewari.ui.expense

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.javs.okanewari.OkaneWareTopAppBar
import com.javs.okanewari.R
import com.javs.okanewari.data.ExpenseModel
import com.javs.okanewari.navigation.NavigationDestination
import com.javs.okanewari.ui.OwViewModelProvider
import com.javs.okanewari.ui.components.ConfirmationDialog
import com.javs.okanewari.ui.components.DateHandler
import com.javs.okanewari.ui.components.DisplayFab
import com.javs.okanewari.ui.components.PartyDetails

object ListExpensesDestination : NavigationDestination {
    override val route = "list_expenses"
    override val titleRes = R.string.list_expenses
    const val partyIdArg = "partyIdArg"
    val routeWithArg = "{$route}/{$partyIdArg}"
}

/**
 * onExpenseCardClick: (List<Int>)
 *      [0] = party id
 *      [1] = expense id
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListExpensesScreen(
    onAddExpenseButtonClicked: (Long) -> Unit,
    onSettingsButtonClicked: (Long) -> Unit,
    onExpenseCardClick: (List<Long>) -> Unit,
    onStatCardClicked: (Long) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListExpensesViewModel = viewModel(factory = OwViewModelProvider.Factory)
){
    val listExpensesUiState by viewModel.listExpensesUiState.collectAsState()
    var checkExpenseLimit by rememberSaveable { mutableStateOf(false) }

    Scaffold (
        topBar = {
            OkaneWareTopAppBar(
                title = listExpensesUiState.partyDetails.partyName,
                canNavigateBack = true,
                navigateUp = navigateUp,
                actionButtons = {
                    IconButton(
                        onClick = {onSettingsButtonClicked(listExpensesUiState.partyDetails.id)}
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
                myClick = { checkExpenseLimit = true },
                contentDescription = stringResource(R.string.add_new_expense)
            )
        }
    ) { innerPadding ->
        ListExpensesBody(
            expenseList = listExpensesUiState.expenseList,
            expenseClicked = onExpenseCardClick,
            statCardClicked = onStatCardClicked,
            partyDetails = listExpensesUiState.partyDetails,
            contentPadding = innerPadding
        )
        if(checkExpenseLimit){
            if (listExpensesUiState.memberList.size < 2){
                ConfirmationDialog(
                    onConfirm = { checkExpenseLimit = false },
                    onCancel = { checkExpenseLimit = false },
                    title = stringResource(R.string.attention),
                    text = "At least 2 members are needed to add an expense.\n\nClick the settings button in the top right to add/modify members.",
                    confirmText = stringResource(R.string.ok),
                    showDismissButton = false
                )
            }
            else if (viewModel.isOverExpenseCountLimit(listExpensesUiState.expenseList.size)){
                ConfirmationDialog(
                    onConfirm = { checkExpenseLimit = false },
                    onCancel = { checkExpenseLimit = false },
                    title = stringResource(R.string.attention),
                    text = "Expense limit reached.",
                    confirmText = stringResource(R.string.ok),
                    showDismissButton = false
                )
            }else{
                checkExpenseLimit = false
                onAddExpenseButtonClicked(listExpensesUiState.partyDetails.id)
            }
        }
    }
}

@Composable
fun ListExpensesBody(
    expenseList: List<ExpenseModel>,
    expenseClicked: (List<Long>) -> Unit,
    statCardClicked: (Long) -> Unit,
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
        DisplayExpensesAndStats(
            expenseList = expenseList,
            expenseClicked = expenseClicked,
            statCardClicked = statCardClicked,
            partyDetails = partyDetails,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun DisplayExpensesAndStats(
    expenseList: List<ExpenseModel>,
    expenseClicked: (List<Long>) -> Unit,
    statCardClicked: (Long) -> Unit,
    partyDetails: PartyDetails,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {

    LazyColumn(
        modifier = Modifier.padding(contentPadding)
    ) {
        item{
            DisplayPartyStats(
                expenseList = expenseList,
                partyDetails = partyDetails,
                currSymbol = partyDetails.currency,
                statCardClicked = statCardClicked
            )
        }
        items(expenseList) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .clickable { expenseClicked(listOf(partyDetails.id, it.id)) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_sell_8),
                        contentDescription = "",
                        modifier = Modifier
                            .width(dimensionResource(R.dimen.thumbnail_width))
                            .height(dimensionResource(R.dimen.thumbnail_height))
                            .padding(all = dimensionResource(R.dimen.medium_padding))
                    )
                    Column {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = partyDetails.currency + it.amount,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = stringResource(R.string.last_modified) + ": "
                                    + DateHandler.formatter.format(it.dateModded),
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayPartyStats(
    expenseList: List<ExpenseModel>,
    partyDetails: PartyDetails,
    currSymbol: String,
    statCardClicked: (Long) -> Unit
){
    Card (
        modifier = Modifier
            .padding(12.dp).fillMaxWidth()
            .clickable { statCardClicked(partyDetails.id) }
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.baseline_insert_chart_8),
                contentDescription = "Party Statistics",
                modifier = Modifier
                    .width(dimensionResource(R.dimen.thumbnail_width))
                    .height(dimensionResource(R.dimen.thumbnail_height))
                    .padding(all = dimensionResource(R.dimen.medium_padding))
            )
            Column{
                Text(
                    text = stringResource(R.string.total) + ": "
                            + currSymbol
                            + getExpenseListSumTotal(expenseList),
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(R.string.number_of_expenses) + ": "
                            + expenseList.count(),
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Click here for split info",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}