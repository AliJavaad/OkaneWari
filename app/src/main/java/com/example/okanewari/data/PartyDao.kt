package com.example.okanewari.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

/*
 * TODO
 * Insert or add a new item.
 * Update an existing item to update the name, price, and quantity.
 * Get a specific item based on its primary key, id.
 * Get all items so you can display them.
 * Delete an entry in the database.
 */

@Dao
interface PartyDao {
    // this will either insert the object if it doesn't exist
    // or update the object if it does already exists in the database
    @Upsert
    suspend fun upsertPartyListModel(partyListModel: PartyModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(party: PartyModel)

    @Update
    suspend fun update(party: PartyModel)

    @Delete
    suspend fun delete(party: PartyModel)

    @Query("SELECT * from partymodel WHERE id = :id")
    fun getParty(id: Int): Flow<PartyModel>

    @Query("SELECT * from partymodel")
    fun getAllRecords(): Flow<List<PartyModel>>

    @Query("SELECT partyName from partymodel")
    fun getAllNames(): Flow<List<String>>

    @RawQuery(observedEntities = [PartyModel::class])
    fun query(query: SupportSQLiteQuery): Flow<List<PartyModel>>
}