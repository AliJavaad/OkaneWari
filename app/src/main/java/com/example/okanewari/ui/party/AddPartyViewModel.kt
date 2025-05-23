package com.example.okanewari.ui.party

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import com.example.okanewari.ui.components.toPartyModel

/**
 * ViewModel to validate and insert Party in the Room database.
 */
class AddPartyViewModel(
    private val owRepository: OkaneWariRepository
): ViewModel() {

    var addPartyUiState by mutableStateOf(PartyUiState())
        private set

    /**
     * Updates the [addPartyUiState] with the value provided in the argument.
     * This method also triggers a validation for input values.
     */
    fun updateUiState(partyDetails: PartyDetails) {
        addPartyUiState =
            PartyUiState(
                partyDetails = partyDetails,
                isEntryValid = validateInput(partyDetails)
            )
    }

    suspend fun saveParty() {
        if (validateInput()) {
            owRepository.insertParty(addPartyUiState.partyDetails.toPartyModel())
        }
    }

    private fun validateInput(uiState: PartyDetails = addPartyUiState.partyDetails): Boolean {
        return with(uiState) {
            partyName.isNotBlank() && currency.isNotBlank() && numberOfMems.isNotBlank()
        }
    }

}
