package com.javs.okanewari.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(member: MemberModel): Long

    @Update
    suspend fun update(member: MemberModel)

    @Delete
    suspend fun delete(member: MemberModel)

    @Query("SELECT * FROM member_table WHERE id = :id AND partyKey = :partyId")
    fun getMember(id: Long, partyId: Long): Flow<MemberModel>

    @Query("SELECT * from member_table WHERE partyKey = :partyId ORDER BY id ASC")
    fun getAllMembersForParty(partyId: Long): Flow<List<MemberModel>>

    @RawQuery(observedEntities = [MemberModel::class])
    fun query(query: SupportSQLiteQuery): Flow<List<MemberModel>>
}