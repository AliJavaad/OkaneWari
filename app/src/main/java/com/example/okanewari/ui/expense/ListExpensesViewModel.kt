package com.example.okanewari.ui.expense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.PartyRepository
import com.example.okanewari.ui.party.AddPartyUiState
import com.example.okanewari.ui.party.PartyDetails
import com.example.okanewari.ui.party.PartyUiState
import com.example.okanewari.ui.party.toPartyUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update and delete an item from the [owRepository]'s data source.
 */
class ListExpensesViewModel(
    savedStateHandle: SavedStateHandle,
    private val partyRepository: PartyRepository
) : ViewModel(){
    /**
     * Holds current item ui state
     */
    var partyUiState by mutableStateOf(PartyUiState())
        private set

    private val partyId: Int = checkNotNull(savedStateHandle[ListExpensesDestination.partyIdArg])

    init {
        viewModelScope.launch {
            partyUiState = partyRepository.getPartyStream(partyId)
                .filterNotNull()
                .first()
                .toPartyUiState(true)
        }
    }
}

