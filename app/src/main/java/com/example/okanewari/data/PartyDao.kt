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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(party: PartyModel)

    @Update
    suspend fun update(party: PartyModel)

    @Delete
    suspend fun delete(party: PartyModel)

    @Query("SELECT * FROM party_table WHERE id = :id")
    fun getParty(id: Int): Flow<PartyModel>

    @Query("SELECT * FROM party_table ORDER BY dateModded DESC")
    fun getAllRecords(): Flow<List<PartyModel>>

    @Query("SELECT partyName FROM party_table")
    fun getAllNames(): Flow<List<String>>

    @RawQuery(observedEntities = [PartyModel::class])
    fun query(query: SupportSQLiteQuery): Flow<List<PartyModel>>
}