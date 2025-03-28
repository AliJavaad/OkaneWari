package com.example.okanewari.ui.party

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.okanewari.data.PartyModel
import com.example.okanewari.data.PartyRepository

/**
 * ViewModel to validate and insert Party in the Room database.
 */
class AddPartyViewModel(
    // private val partyRepository: PartyRepository
): ViewModel() {

    var partyUiState by mutableStateOf(PartyUiState())
        private set

    /**
     * Updates the [partyUiState] with the value provided in the argument.
     * This method also triggers a validation for input values.
     */
    fun updateUiState(partyDetails: PartyDetails) {
        partyUiState =
            PartyUiState(partyDetails = partyDetails, isEntryValid = validateInput(partyDetails))
    }

//    suspend fun saveItem() {
//        if (validateInput()) {
//            partyRepository.insertParty(partyUiState.partyDetails.toPartyModel())
//        }
//    }

    private fun validateInput(uiState: PartyDetails = partyUiState.partyDetails): Boolean {
        return with(uiState) {
            partyName.isNotBlank() && currency.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for a Party.
 */
data class PartyUiState(
    val partyDetails: PartyDetails = PartyDetails(),
    val isEntryValid: Boolean = false
)

data class PartyDetails(
    val id: Int = 0,
    val partyName: String = "",
    val currency: String = "",
    val numberOfMems: String = "",
)

/**
 * Extension function to convert [PartyDetails] to [PartyModel].
 * If the value of [PartyDetails.numberOfMembers] is not a valid [Int],
 * then the numberOfMembers will be set to 1
 */
fun PartyDetails.toPartyModel(): PartyModel = PartyModel(
    id = id,
    partyName = partyName,
    currency = currency,
    numberOfMembers = numberOfMems.toIntOrNull() ?: 1
)

/**
 * Extension function to convert [PartyModel] to [PartyUiState]
 */
fun PartyModel.toPartyUiState(isEntryValid: Boolean = false): PartyUiState = PartyUiState(
    partyDetails = this.toPartyDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [PartyModel] to [PartyDetails]
 */
fun PartyModel.toPartyDetails(): PartyDetails = PartyDetails(
    id = id,
    partyName = partyName,
    currency = currency,
    numberOfMems = numberOfMembers.toString()
)
