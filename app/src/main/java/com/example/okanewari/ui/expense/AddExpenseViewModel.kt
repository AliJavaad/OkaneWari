package com.example.okanewari.ui.expense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.ExpenseModel
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.ui.party.PartyDetails
import com.example.okanewari.ui.party.PartyUiState
import com.example.okanewari.ui.party.toPartyModel
import com.example.okanewari.ui.party.toPartyUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

class AddExpenseViewModel(
    savedStateHandle: SavedStateHandle,
    private val owRepository: OkaneWariRepository
): ViewModel() {

    private val partyId: Int = checkNotNull(savedStateHandle[AddExpenseDestination.partyIdArg])

    var addExpenseUiState by mutableStateOf(AddExpenseUiState(expenseUiState = ExpenseUiState(ExpenseDetails(partyKey = partyId))))
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
            name.isNotBlank() && canConvertStringToBigDecimal(amount)
        }
    }
}

data class AddExpenseUiState(
    var partyUiState: PartyUiState = PartyUiState(PartyDetails()),
    val expenseUiState: ExpenseUiState = ExpenseUiState(ExpenseDetails())
)

/**
 * Represents Ui State for an expense.
 */
data class ExpenseUiState(
    val expenseDetails: ExpenseDetails = ExpenseDetails(),
    val isEntryValid: Boolean = false
)

data class ExpenseDetails(
    val id: Int = 0,
    val partyKey: Int = 0,
    val name: String = "",
    val amount: String = "0.00",
    val dateModded: Date = Date()
)

/**
 * Extension function to convert [ExpenseDetails] to [ExpenseModel].
 * If the value of [ExpenseDetails.numberOfMembers] is not a valid [Int],
 * then the numberOfMembers will be set to 1
 */
fun ExpenseDetails.toExpenseModel(): ExpenseModel = ExpenseModel(
    id = id,
    partyKey = partyKey,
    name = name,
    amount = amount,
    dateModded = dateModded.time
)

/**
 * Extension function to convert [ExpenseModel] to [ExpenseDetails]
 */
fun ExpenseModel.toExpenseUiState(): ExpenseUiState = ExpenseUiState(
    expenseDetails = this.toExpenseDetails()
)

/**
 * Extension function to convert [ExpenseModel] to [ExpenseDetails]
 */
fun ExpenseModel.toExpenseDetails(): ExpenseDetails = ExpenseDetails(
    id = id,
    partyKey = partyKey,
    name = name,
    amount = amount,
    dateModded = Date(dateModded)
)

fun canConvertStringToBigDecimal(value: String): Boolean{
    return value.matches(Regex("^\\d{1,8}(\\.\\d{1,2})?\$"))
}
