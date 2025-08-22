package com.javs.okanewari.ui.party

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.javs.okanewari.data.OkaneWariRepository
import com.javs.okanewari.ui.components.PartyDetails
import com.javs.okanewari.ui.components.PartyUiState
import com.javs.okanewari.ui.components.toPartyModel
import com.javs.okanewari.ui.components.validateNameInput

/**
 * ViewModel to validate and insert Party in the Room database.
 */
class AddPartyViewModel(
    private val owRepository: OkaneWariRepository
): ViewModel() {

    var addPartyUiState by mutableStateOf(AddPartyUiState())
        private set

    /**
     * Updates the [addPartyUiState] with the value provided in the argument.
     * This method also triggers a validation for input values.
     */
    fun updateUiState(partyDetails: PartyDetails) {
        addPartyUiState =
            AddPartyUiState(
                partyUiState = PartyUiState(partyDetails, validatePartyInput(partyDetails))
            )
    }

    /**
     * Saves the party into the table.
     */
    suspend fun saveParty() {
        if (validatePartyInput()) {
            owRepository.insertParty(addPartyUiState.partyUiState.partyDetails.toPartyModel())
        }
    }

    private fun validatePartyInput(uiState: PartyDetails = addPartyUiState.partyUiState.partyDetails): Boolean {
        return with(uiState) {
            validateNameInput(partyName) && currency.isNotBlank()
        }
    }
}

data class AddPartyUiState(
    var partyUiState: PartyUiState = PartyUiState(PartyDetails())
)
