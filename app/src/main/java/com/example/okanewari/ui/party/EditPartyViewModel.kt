package com.example.okanewari.ui.party

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import com.example.okanewari.ui.components.toPartyModel
import com.example.okanewari.ui.components.toPartyUiState
import com.example.okanewari.ui.components.validateNameInput
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update, or delete a party from the [OkaneWariRepository]'s data source.
 */
class EditPartyViewModel(
    savedStateHandle: SavedStateHandle,
    private val owRepository: OkaneWariRepository
) : ViewModel() {

    var editPartyUiState by mutableStateOf(EditPartyUiState())
        private set

    private val partyId: Int = checkNotNull(savedStateHandle[EditPartyDestination.partyIdArg])

    init {
        viewModelScope.launch {
            editPartyUiState.partyUiState = owRepository.getPartyStream(partyId)
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
            owRepository.updateParty(editPartyUiState.partyUiState.partyDetails.toPartyModel())
        }
    }

    /**
     * Updates the [editPartyUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(partyDetails: PartyDetails) {
        editPartyUiState =
            EditPartyUiState(
                partyUiState = PartyUiState(partyDetails, validateInput(partyDetails)),
                topBarPartyName = editPartyUiState.topBarPartyName
            )
    }

    /**
     * Deletes the Party from the [OkaneWariRepository]'s data source.
     */
    suspend fun deleteParty() {
        owRepository.deleteParty(editPartyUiState.partyUiState.partyDetails.toPartyModel())
    }

    private fun validateInput(uiState: PartyDetails = editPartyUiState.partyUiState.partyDetails): Boolean {
        return with(uiState) {
            validateNameInput(partyName) && currency.isNotBlank() && numberOfMems.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for adding a Party.
 */
data class EditPartyUiState(
    var partyUiState: PartyUiState = PartyUiState(PartyDetails()),
    var topBarPartyName: String = ""
)