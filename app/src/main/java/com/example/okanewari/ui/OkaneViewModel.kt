package com.example.okanewari.ui

import androidx.lifecycle.ViewModel
import com.example.okanewari.data.PartyHolderDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OkaneViewModel: ViewModel() {
    // App UI state
    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(OkaneUiState())
    val uiState: StateFlow<OkaneUiState> = _uiState.asStateFlow()

    private lateinit var partyDao: PartyHolderDao
}