package com.example.okanewari.ui.expense

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.ui.party.PartyUiState
import com.example.okanewari.ui.party.toPartyDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve a party and expenses from the [owRepository]'s data source.
 */
class ListExpensesViewModel(
    savedStateHandle: SavedStateHandle,
    private val partyRepository: OkaneWariRepository
) : ViewModel(){

    private val partyId: Int = checkNotNull(savedStateHandle[ListExpensesDestination.partyIdArg])

    /**
     * Holds the party details ui state. The data is retrieved from [partyRepository] and mapped to
     * the UI state.
     */
    val partyUiState: StateFlow<PartyUiState> =
        partyRepository.getPartyStream(partyId)
            .filterNotNull()
            .map { PartyUiState(partyDetails = it.toPartyDetails()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PartyUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}

