package com.example.okanewari.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.okanewari.data.PartyHolderDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel containing the app data and methods to process the data
 */
class OkaneViewModel: ViewModel() {
    // App UI state
    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(OkaneUiState())
    val uiState: StateFlow<OkaneUiState> = _uiState.asStateFlow()

    private lateinit var partyDao: PartyHolderDao

    /**
     * Set the [currentPartyName] as the current state
     */
    fun setCurrentPartyName(partyName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                currentPartyName = partyName
            )
        }
    }
}