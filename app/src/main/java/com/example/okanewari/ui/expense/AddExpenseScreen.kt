package com.example.okanewari.ui.expense

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.DoneAndCancelButtons
import com.example.okanewari.ui.components.ExpenseDetails
import com.example.okanewari.ui.components.ExpenseUiState
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import kotlinx.coroutines.launch
import java.util.Date

object AddExpenseDestination: NavigationDestination{
    override val route = "add_new_expense"
    override val titleRes = R.string.add_new_expense
    const val partyIdArg = "partyIdArg"
    val routeWithArg = "$route/{$partyIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    navigateUp: () -> Unit,
    navigateBack: () -> Unit,
    canNavigateBackBool: Boolean = true,
    viewModel: AddExpenseViewModel = viewModel(factory = OwViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = stringResource(AddExpenseDestination.titleRes),
                canNavigateBack = canNavigateBackBool,
                navigateUp = navigateUp
            )
        }
    ){ innerPadding ->
        ExpenseEntryBody(
            expenseUiState = viewModel.addExpenseUiState.expenseUiState,
            partyUiState = viewModel.addExpenseUiState.partyUiState,
            onValueChange = viewModel::updateUiState,
            onDone = {
                coroutineScope.launch{
                    viewModel.saveExpense()
                    viewModel.updateParty()
                    navigateBack()
                }
            },
            onCancel = navigateBack,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun ExpenseEntryBody(
    expenseUiState: ExpenseUiState,
    partyUiState: PartyUiState,
    onValueChange: (PartyDetails, ExpenseDetails) -> Unit,
    onDone: () -> Unit,
    onCancel: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = Modifier.padding(contentPadding)
    ) {
        ExpenseInputForm(
            expenseDetails = expenseUiState.expenseDetails,
            partyDetails = partyUiState.partyDetails,
            onValueChange = onValueChange
        )
        DoneAndCancelButtons(
            doneButtonClick = onDone,
            cancelButtonClick = onCancel,
            enableDone = expenseUiState.isEntryValid
        )
    }
}

@Composable
fun ExpenseInputForm(
    expenseDetails: ExpenseDetails,
    partyDetails: PartyDetails,
    onValueChange: (PartyDetails, ExpenseDetails) -> Unit,
) {
    /**
     * Handling the expense name input field
     */
    TextField(
        value = expenseDetails.name,
        onValueChange = {
            onValueChange(partyDetails.copy(dateModded = Date()), expenseDetails.copy(name = it, dateModded = Date())) },
        label = { Text(stringResource(R.string.expense_name)) },
        placeholder = { Text(stringResource(R.string.expense)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(R.dimen.medium_padding))
    )
    /**
     * Handling the money input field
     */
    TextField(
        value = expenseDetails.amount,
        onValueChange = {
            onValueChange(partyDetails.copy(dateModded = Date()), expenseDetails.copy(amount = it, dateModded = Date()))
        },
        label = { Text(stringResource(R.string.amount)) },
        placeholder = { Text(stringResource(R.string.zero)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        supportingText = { Text(stringResource(R.string.currency_format_warning)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(R.dimen.medium_padding))
    )
}