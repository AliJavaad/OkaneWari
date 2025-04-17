package com.example.okanewari.ui.expense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.okanewari.data.ExpenseModel
import com.example.okanewari.data.OkaneWariRepository
import java.math.BigDecimal

class AddExpenseViewModel(
    savedStateHandle: SavedStateHandle,
    private val owRepository: OkaneWariRepository
): ViewModel() {

    private val partyId: Int = checkNotNull(savedStateHandle[AddExpenseDestination.partyIdArg])

    var addExpenseUiState by mutableStateOf(ExpenseUiState())
        private set

    private fun validateInput(uiState: ExpenseDetails = addExpenseUiState.expenseDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for a party.
 */
data class ExpenseUiState(
    val expenseDetails: ExpenseDetails = ExpenseDetails(),
    val isEntryValid: Boolean = false
)

data class ExpenseDetails(
    val id: Int = 0,
    val partyKey: Int = 0,
    val name: String = "",
    val amount: BigDecimal = BigDecimal(0.0)
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
    // No negative numbers
    amount = amount.coerceAtLeast(BigDecimal.ZERO).toString()
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
    amount = BigDecimal(amount)
)