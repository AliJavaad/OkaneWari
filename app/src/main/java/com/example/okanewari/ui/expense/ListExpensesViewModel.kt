package com.example.okanewari.ui.expense

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.ExpenseModel
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.toPartyDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal

/**
 * ViewModel to retrieve a party and expenses from the [owRepository]'s data source.
 */
class ListExpensesViewModel(
    savedStateHandle: SavedStateHandle,
    private val owRepository: OkaneWariRepository
) : ViewModel(){

    private val partyId: Int = checkNotNull(savedStateHandle[ListExpensesDestination.partyIdArg])

    /**
     * Holds the screen details ui state. The data is retrieved from [owRepository] and mapped to
     * the UI state.
     */
    val listExpensesUiState: StateFlow<ListExpensesUiState> =
        owRepository.getPartyStream(partyId)
            .filterNotNull()
            .combine(owRepository.getAllExpensesStream(partyId).filterNotNull()){party, expenses ->
                ListExpensesUiState(
                    partyDetails = party.toPartyDetails(),
                    expenseList = expenses
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ListExpensesUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for ListExpensesScreen
 */
data class ListExpensesUiState(
    var partyDetails: PartyDetails = PartyDetails(),
    var expenseList: List<ExpenseModel> = listOf()
)

/**
 * Gets an expenseList of type Expense Models.
 * Calculates the sum total of expenses in BigDecimal form.
 * Does NOT need to round bc numbers will always be within 2 decimal places.
 * Returns the number in String form.
 */
fun getExpenseListSumTotal(expenseList: List<ExpenseModel>): String {
    var sum = BigDecimal.ZERO
    for (expense in expenseList){
        sum += BigDecimal(expense.amount)
    }
    return sum.toString()
}