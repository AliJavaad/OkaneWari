package com.javs.okanewari.ui.party

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javs.okanewari.data.MemberModel
import com.javs.okanewari.data.OkaneWariRepository
import com.javs.okanewari.ui.components.LimitHolder
import com.javs.okanewari.ui.components.PartyDetails
import com.javs.okanewari.ui.components.PartyUiState
import com.javs.okanewari.ui.components.toPartyModel
import com.javs.okanewari.ui.components.toPartyUiState
import com.javs.okanewari.ui.components.validateNameInput
import kotlinx.coroutines.ensureActive
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

    private val partyId: Long = checkNotNull(savedStateHandle[EditPartyDestination.partyIdArg])

    init {
        viewModelScope.launch {
            try {
                // One time get for party
                val partyModel = owRepository.getPartyStream(partyId)
                    .filterNotNull()
                    .first()
                // The topBarPartyName should only be updated at the initial screen creation stage.
                // Otherwise it will keep changing as the text field/party name is edited.
                editPartyUiState = editPartyUiState.copy(
                    partyUiState = partyModel.toPartyUiState(true),
                    topBarPartyName = partyModel.partyName
                )
                // Reactive flow state for member list
                owRepository.getAllMembersFromParty(partyId)
                    .filterNotNull()
                    .collect{ dbMembers ->
                        editPartyUiState = editPartyUiState.copy(
                            memberList = dbMembers
                        )
                    }
            }catch (e: Exception){
                coroutineContext.ensureActive()
                Log.e("EditPartyVM", "Failed to initialize data.", e)
            }
        }
    }

    suspend fun updateParty() {
        if (validateInput()) {
            owRepository.updateParty(editPartyUiState.partyUiState.partyDetails.toPartyModel())
        }
    }

    /**
     * Updates only the partyUiState [editPartyUiState] with the value provided in the argument.
     * This method also triggers a validation for input values.
     */
    fun updatePartyUiState(partyDetails: PartyDetails) {
        editPartyUiState = editPartyUiState.copy(
            partyUiState = PartyUiState(partyDetails, validateInput(partyDetails))
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
            validateNameInput(partyName) && currency.isNotBlank()
        }
    }

    fun isOverMemberCountLimit(size: Int): Boolean{
        return (size > LimitHolder.memberCountLimit)
    }
}

/**
 * Represents Ui State for editing a Party.
 */
data class EditPartyUiState(
    var partyUiState: PartyUiState = PartyUiState(PartyDetails()),
    var memberList: List<MemberModel> = listOf(),
    var topBarPartyName: String = ""
)