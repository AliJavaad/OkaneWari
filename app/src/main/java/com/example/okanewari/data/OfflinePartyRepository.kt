package com.example.okanewari.data

import kotlinx.coroutines.flow.Flow

class OfflinePartyRepository(private val partyDao: PartyDao): PartyRepository {
    override fun getAllPartiesStream(): Flow<List<PartyModel>> = partyDao.getAllRecords()

    override fun getPartyStream(id: Int): Flow<PartyModel?> = partyDao.getParty(id)

    override suspend fun insertParty(party: PartyModel) = partyDao.insert(party)

    override suspend fun deleteParty(party: PartyModel) = partyDao.delete(party)

    override suspend fun updateParty(party: PartyModel) = partyDao.update(party)
}