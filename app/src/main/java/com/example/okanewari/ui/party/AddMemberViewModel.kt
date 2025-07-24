package com.example.okanewari.ui.party

import android.util.Log
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
import com.example.okanewari.ui.components.toPartyModel
import com.example.okanewari.ui.components.toPartyUiState
import com.example.okanewari.ui.components.validateNameInput
import com.example.okanewari.ui.expense.AddExpenseDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddMemberViewModel(
    savedStateHandle: SavedStateHandle,
    private val owRepository: OkaneWariRepository
): ViewModel() {

    private val partyId: Long = checkNotNull(savedStateHandle[AddExpenseDestination.partyIdArg])

    var addMemberUiState by mutableStateOf(AddMemberUiState(memberUiState = MemberUiState(
        MemberDetails(partyKey = partyId))))
        private set

    // Get the initial party info when entering the screen
    init {
        viewModelScope.launch {
            try{
                val partyModel = owRepository.getPartyStream(partyId)
                    .filterNotNull()
                    .first()
                addMemberUiState = addMemberUiState.copy(
                    partyUiState = partyModel.toPartyUiState(true)
                )
            }catch (e:Exception){
                Log.e("AddMemberVM", "Failed to initialize data.", e)
            }

        }
    }

    fun updateUiState(partyDetails: PartyDetails, memberDetails: MemberDetails) {
        addMemberUiState =
            AddMemberUiState(
                memberUiState = MemberUiState(memberDetails, validateMember(memberDetails)),
                partyUiState = PartyUiState(partyDetails, true)
            )
    }

    suspend fun saveMember() {
        if (validateMember()) {
            try {
                owRepository.insertMember(addMemberUiState.memberUiState.memberDetails.toMemberModel())
            }catch (e: Exception){
                Log.e("AddMemberVM", "Failed to save the member.", e)
            }
        }
    }

    suspend fun updateParty(){
        try{
            owRepository.updateParty(addMemberUiState.partyUiState.partyDetails.toPartyModel())
        }catch (e: Exception){
            Log.e("AddMemberVM", "Failed to update the party.", e)
        }
    }

    private fun validateMember(uiState: MemberDetails = addMemberUiState.memberUiState.memberDetails): Boolean {
        return with(uiState) {
            validateNameInput(name)
        }
    }
}

data class AddMemberUiState(
    var memberUiState: MemberUiState = MemberUiState(MemberDetails()),
    var partyUiState: PartyUiState = PartyUiState(PartyDetails())
)