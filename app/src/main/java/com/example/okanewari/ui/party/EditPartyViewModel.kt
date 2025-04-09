package com.example.okanewari.ui.party

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.PartyRepository
import com.example.okanewari.ui.expense.ListExpensesDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update a party from the [PartyRepository]'s data source.
 */
class EditPartyViewModel(
    savedStateHandle: SavedStateHandle,
    private val partyRepository: PartyRepository
) : ViewModel() {

    var editPartyUiState by mutableStateOf(AddPartyUiState())
        private set

    private val partyId: Int = checkNotNull(savedStateHandle[EditPartyDestination.partyIdArg])

    init {
        viewModelScope.launch {
            editPartyUiState.partyUiState = partyRepository.getPartyStream(partyId)
                .filterNotNull()
                .first()
                .toPartyUiState(true)
        }
    }

    suspend fun updateParty() {
        if (validateInput()) {
            partyRepository.updateParty(editPartyUiState.partyUiState.partyDetails.toPartyModel())
        }
    }

    /**
     * Updates the [editPartyUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(partyDetails: PartyDetails, currencyDropdown: Boolean) {
        editPartyUiState =
            AddPartyUiState(
                partyUiState = PartyUiState(partyDetails, validateInput(partyDetails)),
                currencyDropdown = currencyDropdown
            )
    }

    private fun validateInput(uiState: PartyDetails = editPartyUiState.partyUiState.partyDetails): Boolean {
        return with(uiState) {
            partyName.isNotBlank() && currency.isNotBlank() && numberOfMems.isNotBlank()
        }
    }
}