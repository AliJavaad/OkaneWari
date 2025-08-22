package com.javs.okanewari.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 * [id]: Unique key so partys can have same name
 * [partyName]: party name that should be displayed
 * [numberOfMembers]: current number of members in the party
 * [currency]: the currency prefix for displaying numbers
 * [dateModded]: the date in yyyy-mm-dd
 */
@Entity(tableName = "party_table")
data class PartyModel (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val partyName: String,
    val currency: String,
    val dateModded: Long
)