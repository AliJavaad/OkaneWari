package com.example.okanewari.ui.party

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.ui.components.MemberDetails
import com.example.okanewari.ui.components.MemberUiState
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import com.example.okanewari.ui.components.toMemberModel
import com.example.okanewari.ui.components.toMemberUiState
import com.example.okanewari.ui.components.toPartyModel
import com.example.okanewari.ui.components.toPartyUiState
import com.example.okanewari.ui.components.validateNameInput
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update, or delete a member from the [OkaneWariRepository]'s data source.
 */
class EditMemberViewModel(
    savedStateHandle: SavedStateHandle,
    private val owRepository: OkaneWariRepository
) : ViewModel() {

    var editMemberUiState by mutableStateOf(EditMemberUiState())
        private set

    private val partyId: Long = checkNotNull(savedStateHandle[EditMemberDestination.partyIdArg])
    private val memberId: Long = checkNotNull(savedStateHandle[EditMemberDestination.memberIdArg])

    // Get the initial party and member info when entering the screen
    init {
        viewModelScope.launch {
            editMemberUiState.partyUiState = owRepository.getPartyStream(partyId)
                .filterNotNull()
                .first()
                .toPartyUiState(true)
            editMemberUiState.memberUiState = owRepository.getMember(memberId, partyId)
                .filterNotNull()
                .first()
                .toMemberUiState(true)
            editMemberUiState.topBarPartyName = editMemberUiState.memberUiState.memberDetails.name
        }
    }

    fun updateUiState(partyDetails: PartyDetails, memberDetails: MemberDetails) {
        editMemberUiState =
            EditMemberUiState(
                memberUiState = MemberUiState(memberDetails, validateMember(memberDetails)),
                partyUiState = PartyUiState(partyDetails, true),
                topBarPartyName = editMemberUiState.topBarPartyName
            )
    }

    suspend fun updateMember() {
        if (validateMember()) {
            owRepository.updateMember(editMemberUiState.memberUiState.memberDetails.toMemberModel())
        }
    }

    suspend fun deleteMember(){
        owRepository.deleteMember(editMemberUiState.memberUiState.memberDetails.toMemberModel())
    }

    suspend fun updateParty(){
        owRepository.updateParty(editMemberUiState.partyUiState.partyDetails.toPartyModel())
    }

    private fun validateMember(uiState: MemberDetails = editMemberUiState.memberUiState.memberDetails): Boolean {
        return with(uiState) {
            validateNameInput(name)
        }
    }
}

/**
 * Represents Ui State for editing a Party.
 */
data class EditMemberUiState(
    var memberUiState: MemberUiState = MemberUiState(MemberDetails()),
    var partyUiState: PartyUiState = PartyUiState(PartyDetails()),
    var topBarPartyName: String = ""
)