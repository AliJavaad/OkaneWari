package com.example.okanewari.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expense_table",
    foreignKeys = [
        ForeignKey(
            entity = PartyModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("partyKey"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExpenseModel (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val partyKey: Long,
    val name: String,
    val amount: String,
    val dateModded: Long
)