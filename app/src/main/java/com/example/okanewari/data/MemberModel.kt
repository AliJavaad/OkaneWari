package com.example.okanewari.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "member_table",
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
data class MemberModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val partyKey: Int,
    val name: String,
    val owner: Boolean
)