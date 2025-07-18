package com.example.okanewari.ui.expense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.MemberModel
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.ui.components.MemberDetails
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import com.example.okanewari.ui.components.toPartyUiState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ShowSplitsViewModel(
    savedStateHandle: SavedStateHandle,
    private val owRepository: OkaneWariRepository
): ViewModel() {
    private val partyId: Long = checkNotNull(savedStateHandle[EditExpenseDestination.partyIdArg])

    var showSplitsUiState by mutableStateOf(ShowSplitsUiState())
        private set

    init {
        viewModelScope.launch {
            // Get party info
            val partyModel = owRepository.getPartyStream(partyId)
                .filterNotNull()
                .first()
            showSplitsUiState = showSplitsUiState.copy(
                partyUiState = partyModel.toPartyUiState(true),
                topBarExpenseName = partyModel.partyName
            )

            // Load and parse the Member and split info
            owRepository.getAllMembersFromParty(partyId)
                .filterNotNull()
                .combine(
                    owRepository.getAllSplitsForParty(partyId).filterNotNull()
                ) { allMems, allSplits ->
                    Pair(allMems, allSplits)
                }
                .collect { (rawMembers, splitList) ->
                    // Process the members
                    val memberMap = rawMembers.associateBy { it.id }

                    // Process Splits
                    val memberSplitTotals =
                        memberMap.map { it.key to BigDecimal.ZERO }.toMap().toMutableMap()

                    splitList.forEach { split ->
                        memberSplitTotals[split.memberKey] =
                            BigDecimal(split.splitAmount).add(memberSplitTotals[split.memberKey])
                    }

                    // Update the state
                    showSplitsUiState = showSplitsUiState.copy(
                        memberList = memberMap,
                        memberSplitTotals = memberSplitTotals
                    )
                }
        }
    }
}

/**
 * Represents Ui State for Showing the splits.
 */
data class ShowSplitsUiState(
    var partyUiState: PartyUiState = PartyUiState(PartyDetails()),
    var memberList: Map<Long, MemberModel> = mapOf(),
    var memberSplitTotals: Map<Long, BigDecimal> = mapOf(),
    var topBarExpenseName: String = "",
)