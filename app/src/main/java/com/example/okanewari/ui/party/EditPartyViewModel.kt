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
 * ViewModel to retrieve, update, or delete a party from the [PartyRepository]'s data source.
 */
class EditPartyViewModel(
    savedStateHandle: SavedStateHandle,
    private val partyRepository: PartyRepository
) : ViewModel() {

    var editPartyUiState by mutableStateOf(EditPartyUiState())
        private set

    private val partyId: Int = checkNotNull(savedStateHandle[EditPartyDestination.partyIdArg])

    init {
        viewModelScope.launch {
            editPartyUiState.partyUiState = partyRepository.getPartyStream(partyId)
                .filterNotNull()
                .first()
                .toPartyUiState(true)
            // The topBarPartyName should only be updated at the initial screen creation stage.
            // Otherwise it will keep changing as the text field/party name is edited.
            editPartyUiState.topBarPartyName = editPartyUiState.partyUiState.partyDetails.partyName
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
            EditPartyUiState(
                partyUiState = PartyUiState(partyDetails, validateInput(partyDetails)),
                currencyDropdown = currencyDropdown,
                topBarPartyName = editPartyUiState.topBarPartyName
            )
    }

    private fun validateInput(uiState: PartyDetails = editPartyUiState.partyUiState.partyDetails): Boolean {
        return with(uiState) {
            partyName.isNotBlank() && currency.isNotBlank() && numberOfMems.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for adding a Party.
 */
data class EditPartyUiState(
    var partyUiState: PartyUiState = PartyUiState(PartyDetails()),
    var topBarPartyName: String = "",
    val currencyDropdown: Boolean = false
)