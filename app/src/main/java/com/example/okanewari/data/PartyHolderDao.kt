package com.example.okanewari.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface PartyHolderDao {
    // this will either insert the object if it doesn't exist
    // or update the object if it does already exists in the database
    @Upsert
    suspend fun upsertPartyListModel(partyListModel: PartyHolderModel)

    @Query("SELECT * from partyholdermodel")
    fun getAllRecords(): Flow<List<PartyHolderModel>>

    @Query("SELECT partyName from partyholdermodel")
    fun getAllNames(): Flow<List<String>>

    @RawQuery(observedEntities = [PartyHolderModel::class])
    fun query(query: SupportSQLiteQuery): Flow<List<PartyHolderModel>>
}