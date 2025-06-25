package com.example.okanewari.ui.party

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.ui.components.MemberDetails
import com.example.okanewari.ui.components.MemberUiState
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import com.example.okanewari.ui.components.toMemberModel
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
    fun updateUiState(partyDetails: PartyDetails, memberDetails: MemberDetails) {
        addPartyUiState =
            AddPartyUiState(
                partyUiState = PartyUiState(partyDetails, validatePartyInput(partyDetails)),
                memberUiState = MemberUiState(memberDetails, validateMemberInput(memberDetails))
            )
    }

    fun updatePartyUiState(partyDetails: PartyDetails) {
        addPartyUiState =
            AddPartyUiState(
                partyUiState = PartyUiState(partyDetails, validatePartyInput(partyDetails)),
                memberUiState = addPartyUiState.memberUiState
            )
    }

    fun updateMemberUiState(memberDetails: MemberDetails) {
        addPartyUiState =
            AddPartyUiState(
                partyUiState = addPartyUiState.partyUiState,
                memberUiState = MemberUiState(memberDetails, validateMemberInput(memberDetails))
            )
    }


    /**
     * Saves the party and host member into the tables.
     * They MUST be sequential as the party must get stored first so the member can properly
     * reference the foreign key.
     */
    suspend fun savePartyAndHostMember() {
        if (validatePartyInput() && validateMemberInput()) {
            val partyKey = owRepository.insertParty(addPartyUiState.partyUiState.partyDetails.toPartyModel())
            owRepository.insertMember(
                addPartyUiState
                    .memberUiState.memberDetails.copy(partyKey = partyKey, owner = true)
                    .toMemberModel()
            )
        }
    }

    private fun validatePartyInput(uiState: PartyDetails = addPartyUiState.partyUiState.partyDetails): Boolean {
        return with(uiState) {
            validateNameInput(partyName) && currency.isNotBlank() && numberOfMems.isNotBlank()
        }
    }

    private fun validateMemberInput(uiState: MemberDetails = addPartyUiState.memberUiState.memberDetails): Boolean {
        return with(uiState){
            validateNameInput(name)
        }
    }
}

data class AddPartyUiState(
    var partyUiState: PartyUiState = PartyUiState(PartyDetails()),
    var memberUiState: MemberUiState = MemberUiState(MemberDetails())
)
