package com.example.okanewari.data

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
interface SplitDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expSplit: SplitModel)

    @Update
    suspend fun update(expSplit: SplitModel)

    @Delete
    suspend fun delete(expSplit: SplitModel)

    @Query("SELECT * FROM split_table WHERE id = :id")
    fun getSplit(id: Long): Flow<SplitModel>

    @Query("SELECT * FROM split_table WHERE expenseKey = :expenseKey AND memberKey = :memberKey")
    fun getSplitFromExpAndMemKeys(expenseKey: Long, memberKey: Long): Flow<SplitModel>

    @Query("SELECT * FROM split_table WHERE expenseKey = :expenseKey")
    fun getAllSplitsForExpense(expenseKey: Long): Flow<List<SplitModel>>

    @Query("SELECT * FROM split_table WHERE memberKey = :memberKey")
    fun getAllSplitsForMember(memberKey: Long): Flow<List<SplitModel>>

    @RawQuery(observedEntities = [SplitModel::class])
    fun query(query: SupportSQLiteQuery): Flow<List<SplitModel>>
}