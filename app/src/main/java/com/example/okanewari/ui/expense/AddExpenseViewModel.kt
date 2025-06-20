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
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import com.example.okanewari.ui.components.toPartyModel
import com.example.okanewari.ui.components.toPartyUiState
import com.example.okanewari.ui.components.validateNameInput
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddExpenseViewModel(
    savedStateHandle: SavedStateHandle,
    private val owRepository: OkaneWariRepository
): ViewModel() {

    private val partyId: Long = checkNotNull(savedStateHandle[AddExpenseDestination.partyIdArg])

    var addExpenseUiState by mutableStateOf(AddExpenseUiState(expenseUiState = ExpenseUiState(
        ExpenseDetails(partyKey = partyId))))
        private set

    // Get the initial party info when entering the screen
    init {
        viewModelScope.launch {
            addExpenseUiState.partyUiState = owRepository.getPartyStream(partyId)
                .filterNotNull()
                .first()
                .toPartyUiState(true)
        }
    }

    /**
     * Updates the [addExpenseUiState] with the value provided in the argument.
     * This method also triggers a validation for input values.
     */
    fun updateUiState(partyDetails: PartyDetails, expenseDetails: ExpenseDetails) {
        addExpenseUiState =
            AddExpenseUiState(
                expenseUiState = ExpenseUiState(expenseDetails, validateExpense(expenseDetails)),
                partyUiState = PartyUiState(partyDetails, true)
            )
    }

    suspend fun saveExpense() {
        if (validateExpense()) {
            owRepository.insertExpense(addExpenseUiState.expenseUiState.expenseDetails.toExpenseModel())
        }
    }

    suspend fun updateParty(){
        owRepository.updateParty(addExpenseUiState.partyUiState.partyDetails.toPartyModel())
    }

    private fun validateExpense(uiState: ExpenseDetails = addExpenseUiState.expenseUiState.expenseDetails): Boolean {
        return with(uiState) {
            validateNameInput(name) && canConvertStringToBigDecimal(amount)
        }
    }
}

data class AddExpenseUiState(
    var partyUiState: PartyUiState = PartyUiState(PartyDetails()),
    val expenseUiState: ExpenseUiState = ExpenseUiState(ExpenseDetails())
)


