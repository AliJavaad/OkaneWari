package com.example.okanewari.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel containing the app data and methods to process the data
 */
class OkaneViewModel(
    // private val partyDao: PartyHolderDao
): ViewModel() {
    // App UI state
    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(OkaneUiState())
    val uiState: StateFlow<OkaneUiState> = _uiState.asStateFlow()

    // private lateinit var partyDao: PartyHolderDao

    // ================ General Methods ====================
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
    /*
    // ========== Party Holder Table Methods ==============
    /**
     * Adds dummy data for testing purposes
     */
    fun addDummyDataPh() {
        val nameList = DummyPartyNameList()
        viewModelScope.launch {
            nameList.forEach {
                val newDataToAdd = PartyHolderModel(
                    partyName = it,
                    currency = "$",
                    numberOfMembers = 1
                )
                partyDao.upsertPartyListModel(newDataToAdd)
            }
        }
    }

    /**
     * Adds new party to the table.
     * [name]: Name of the new party
     */
    fun addDataPh(name: String){
        viewModelScope.launch{
            partyDao.upsertPartyListModel(
                PartyHolderModel(
                    partyName = name,
                    currency = "$",
                    numberOfMembers = 1
                )
            )
        }
    }
    */
}