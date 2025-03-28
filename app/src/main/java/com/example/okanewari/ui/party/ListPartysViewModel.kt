package com.example.okanewari.ui.party

import androidx.lifecycle.ViewModel
import com.example.okanewari.data.PartyModel

/**
 * ViewModel to retrieve all items in the Room database.
 */
class ListPartysViewModel(): ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for ListPartysScreen
 */
data class HomeUiState(
    // TODO holder for expense table as well
    val partyList: List<PartyModel> = listOf()
)