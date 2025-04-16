package com.example.okanewari.ui.expense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.ExpenseModel
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.data.PartyModel
import com.example.okanewari.ui.party.EditPartyUiState
import com.example.okanewari.ui.party.ListPartysUiState
import com.example.okanewari.ui.party.ListPartysViewModel
import com.example.okanewari.ui.party.ListPartysViewModel.Companion
import com.example.okanewari.ui.party.PartyDetails
import com.example.okanewari.ui.party.PartyUiState
import com.example.okanewari.ui.party.toPartyDetails
import com.example.okanewari.ui.party.toPartyUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
