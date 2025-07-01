package com.example.okanewari.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * A "split" represents the split costs for one expense.
 * It tracks the burden of each member for each specific expense.
 */
@Entity(
    tableName = "split_table",
    foreignKeys = [
        ForeignKey(
            entity = PartyModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("partyKey"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExpenseModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("expenseKey"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MemberModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("memberKey"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SplitModel (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val partyKey: Long,
    val expenseKey: Long,
    val memberKey: Long,
    val splitAmount: String
)