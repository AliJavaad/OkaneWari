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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.DoneAndCancelButtons
import kotlinx.coroutines.launch

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
            expenseUiState = viewModel.addExpenseUiState,
            onValueChange = viewModel::updateUiState,
            onDone = {
                coroutineScope.launch{
                    // viewModel.saveExpense()
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
    onValueChange: (ExpenseDetails) -> Unit,
    onDone: () -> Unit,
    onCancel: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = Modifier.padding(contentPadding)
    ) {
        ExpenseInputForm(
            expenseDetails = expenseUiState.expenseDetails,
            onValueChange = onValueChange
        )
        DoneAndCancelButtons(
            doneButtonClick = onDone,
            cancelButtonClick = onCancel
        )
    }
}

@Composable
fun ExpenseInputForm(
    expenseDetails: ExpenseDetails,
    onValueChange: (ExpenseDetails) -> Unit,
) {
    /**
     * Handling the expense name input field
     */
    TextField(
        value = expenseDetails.name,
        onValueChange = { onValueChange(expenseDetails.copy(name = it)) },
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
    var showWarning by rememberSaveable { mutableStateOf(false) }
    TextField(
        value = expenseDetails.amount,
        onValueChange = {
            showWarning = !canConvertStringToBigDecimal(it)
            onValueChange(expenseDetails.copy(amount = it))
        },
        label = { Text(stringResource(R.string.amount)) },
        placeholder = { Text(stringResource(R.string.zero)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(R.dimen.medium_padding))
    )
    if(showWarning){
        Text(
            stringResource(R.string.currency_format_warning),
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.medium_padding))
        )
    }
}