package com.example.okanewari.ui.party

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.PartyModel
import com.example.okanewari.data.PartyRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all items in the Room database.
 */
class ListPartysViewModel(partyRepository: PartyRepository): ViewModel() {
    /**
     * The list of parties are retrieved from [PartyRepository] and mapped to [ListPartysUiState].
     * Convert a Flow to a StateFlow, using stateIn operator.
     */
    val listPartysUiState: StateFlow<ListPartysUiState> =
        partyRepository.getAllPartiesStream().map{ ListPartysUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ListPartysUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for ListPartysScreen
 */
data class ListPartysUiState(
    val partyList: List<PartyModel> = listOf()
)