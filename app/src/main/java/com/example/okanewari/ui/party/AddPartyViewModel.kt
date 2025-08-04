package com.example.okanewari.ui.party

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import com.example.okanewari.ui.components.toPartyModel
import com.example.okanewari.ui.components.validateNameInput

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
