package com.example.okanewari.ui.party

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.PartyModel
import com.example.okanewari.data.OkaneWariRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all items in the Room database.
 */
class ListPartysViewModel(owRepository: OkaneWariRepository): ViewModel() {
    /**
     * The list of parties are retrieved from [OkaneWariRepository] and mapped to [ListPartysUiState].
     * Convert a Flow to a StateFlow, using stateIn operator.
     */
    val listPartysUiState: StateFlow<ListPartysUiState> =
        owRepository.getAllPartiesStream().map{ ListPartysUiState(it) }
            .catch { e ->
                Log.e("ListPartysVM", "Failed to initialize data.", e)
            }
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