package com.example.okanewari.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PartyHolderViewModel(
    private val dao: PartyDao
): ViewModel() {
    // Adds dummy data for testing purposes
    fun addDummyData() {
        val nameList = DummyPartyNameList()
        viewModelScope.launch {
            nameList.forEach {
                val newDataToAdd = PartyModel(
                    partyName = it,
                    currency = "$",
                    numberOfMembers = 1
                )
                dao.upsertPartyListModel(newDataToAdd)
            }
        }
    }
    // Adds new party.
    // entryTableName param is left empty as no entries are in yet
    fun addData(name: String){
        viewModelScope.launch{
            dao.upsertPartyListModel(
                PartyModel(
                    partyName = name,
                    currency = "$",
                    numberOfMembers = 1
                )
            )
        }
    }
}