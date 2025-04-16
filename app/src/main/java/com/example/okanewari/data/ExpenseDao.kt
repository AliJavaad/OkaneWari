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
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expense: ExpenseModel)

    @Update
    suspend fun update(expense: ExpenseModel)

    @Delete
    suspend fun delete(expense: ExpenseModel)

    @Query("SELECT * from expense_table WHERE id = :id AND partyKey = :partyId")
    fun getExpense(id: Int, partyId: Int): Flow<ExpenseModel>

    @Query("SELECT * from expense_table WHERE partyKey = :partyId")
    fun getAllExpensesForParty(partyId: Int): Flow<List<ExpenseModel>>

    @RawQuery(observedEntities = [ExpenseModel::class])
    fun query(query: SupportSQLiteQuery): Flow<List<ExpenseModel>>
}