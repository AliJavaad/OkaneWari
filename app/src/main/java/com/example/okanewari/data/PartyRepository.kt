package com.example.okanewari.data

import kotlinx.coroutines.flow.Flow

interface PartyRepository {
    /**
     * Retrieve all the parties from the the given data source.
     */
    fun getAllPartiesStream(): Flow<List<PartyModel>>

    /**
     * Retrieve a party from the given data source that matches with the [id].
     */
    fun getPartyStream(id: Int): Flow<PartyModel?>

    /**
     * Insert party in the data source
     */
    suspend fun insertParty(party: PartyModel)

    /**
     * Delete party from the data source
     */
    suspend fun deleteParty(party: PartyModel)

    /**
     * Update party in the data source
     */
    suspend fun updateParty(party: PartyModel)
}