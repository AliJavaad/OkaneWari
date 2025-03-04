package com.example.okanewari.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PartyHolderViewModel(
    private val dao: PartyHolderDao
): ViewModel() {
    // Adds dummy data for testing purposes
    fun addDummyData() {
        val nameList = DummyPartyNameList()
        viewModelScope.launch {
            nameList.forEach {
                val newDataToAdd = PartyHolderModel(
                    partyName = it
                )
                dao.upsertPartyListModel(newDataToAdd)
            }
        }
    }
    // Adds new party.
    // entryTableName param is left empty as no entries are in yet
    fun addData(name: String){
        viewModelScope.launch{
            dao.upsertPartyListModel(PartyHolderModel(partyName = name))
        }
    }
}