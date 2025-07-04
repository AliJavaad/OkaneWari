package com.example.okanewari.ui.expense

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.DoneCancelDeleteButtons
import kotlinx.coroutines.launch
import java.util.Date

object EditExpenseDestination: NavigationDestination{
    override val route = "edit_expense"
    override val titleRes = R.string.edit_expense
    const val partyIdArg = "partyIdArg"
    const val expenseIdArg = "expenseIdArg"
    val routeWithArg = "$route/{$partyIdArg}/{$expenseIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExpenseScreen(
    navigateUp: () -> Unit,
    navigateBack: () -> Unit,
    canNavigateBackBool: Boolean = true,
    viewModel: EditExpenseViewModel = viewModel(factory = OwViewModelProvider.Factory)
){
    Log.d("PartyKey", "AddExpenseScreen Party Key: ${viewModel.editExpenseUiState.expenseUiState.expenseDetails.partyKey}")
    Log.d("ExpenseKey", "AddExpenseScreen Expense Key: ${viewModel.editExpenseUiState.expenseUiState.expenseDetails.id}")

    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = stringResource(R.string.expense)+ ": " + viewModel.editExpenseUiState.topBarExpenseName,
                canNavigateBack = canNavigateBackBool,
                navigateUp = navigateUp
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ){
            ExpenseInputForm(
                expenseDetails = viewModel.editExpenseUiState.expenseUiState.expenseDetails,
                partyDetails = viewModel.editExpenseUiState.partyUiState.partyDetails,
                onValueChange = viewModel::updateUiState
            )
            DoneCancelDeleteButtons(
                doneButtonClick = { coroutineScope.launch{
                    viewModel.updateExpense()
                    viewModel.updateParty()
                    navigateBack()
                } },
                cancelButtonClick = navigateBack,
                deleteButtonClicked = {
                    // Need to update the date modified for the party details so delete is reflected
                    viewModel.updateUiState(
                        viewModel.editExpenseUiState.partyUiState.partyDetails.copy(dateModded = Date()),
                        viewModel.editExpenseUiState.expenseUiState.expenseDetails.copy())
                    coroutineScope.launch{
                        viewModel.deleteExpense()
                        viewModel.updateParty()
                        navigateBack()
                    } },
                enableDone = viewModel.editExpenseUiState.expenseUiState.isEntryValid
            )
        }
    }
}

