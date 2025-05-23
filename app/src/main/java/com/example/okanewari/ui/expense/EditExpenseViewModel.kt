package com.example.okanewari.ui.expense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.ui.components.ExpenseDetails
import com.example.okanewari.ui.components.ExpenseUiState
import com.example.okanewari.ui.components.canConvertStringToBigDecimal
import com.example.okanewari.ui.components.toExpenseModel
import com.example.okanewari.ui.components.toExpenseUiState
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import com.example.okanewari.ui.components.toPartyModel
import com.example.okanewari.ui.components.toPartyUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditExpenseViewModel(
    savedStateHandle: SavedStateHandle,
    private val owRepository: OkaneWariRepository
): ViewModel() {
    private val partyId: Int = checkNotNull(savedStateHandle[EditExpenseDestination.partyIdArg])
    private val expenseId: Int = checkNotNull(savedStateHandle[EditExpenseDestination.expenseIdArg])

    var editExpenseUiState by mutableStateOf(EditExpenseUiState())
        private set

    init {
        viewModelScope.launch {
            editExpenseUiState.expenseUiState = owRepository.getExpense(expenseId, partyId)
                .filterNotNull()
                .first()
                .toExpenseUiState()
            editExpenseUiState.partyUiState = owRepository.getPartyStream(partyId)
                .filterNotNull()
                .first()
                .toPartyUiState()
            // The topBarExpenseName should only be updated at the initial screen creation stage.
            // Otherwise it will keep changing as the text field name is edited.
            editExpenseUiState.topBarExpenseName =
                editExpenseUiState.expenseUiState.expenseDetails.name
        }
    }

    fun updateUiState(partyDetails: PartyDetails, expenseDetails: ExpenseDetails) {
        editExpenseUiState =
            EditExpenseUiState(
                expenseUiState = ExpenseUiState(expenseDetails, validateInput(expenseDetails)),
                partyUiState = PartyUiState(partyDetails, true),
                topBarExpenseName = editExpenseUiState.topBarExpenseName
            )
    }

    suspend fun updateExpense() {
        if (validateInput()) {
            owRepository.updateExpense(editExpenseUiState.expenseUiState.expenseDetails.toExpenseModel())
        }
    }

    suspend fun updateParty(){
        owRepository.updateParty(editExpenseUiState.partyUiState.partyDetails.toPartyModel())
    }

    /**
     * Deletes the expense from the [OkaneWariRepository]'s data source.
     */
    suspend fun deleteExpense() {
        owRepository.deleteExpense(editExpenseUiState.expenseUiState.expenseDetails.toExpenseModel())
    }

    private fun validateInput(uiState: ExpenseDetails = editExpenseUiState.expenseUiState.expenseDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && canConvertStringToBigDecimal(amount)
        }
    }
}

/**
 * Represents Ui State for Editing an expense.
 */
data class EditExpenseUiState(
    var expenseUiState: ExpenseUiState = ExpenseUiState(ExpenseDetails()),
    var partyUiState: PartyUiState = PartyUiState(PartyDetails()),
    var topBarExpenseName: String = ""
)