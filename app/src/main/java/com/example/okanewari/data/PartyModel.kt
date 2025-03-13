package com.example.okanewari.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */
@Entity
data class PartyModel (
    /**
     * [id]: Unique key so partys can have same name
     * [partyName]: party name that should be displayed
     * [numberOfMembers]: current number of members in the party
     * [currency]: the currency prefix for displaying numbers
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val partyName: String,
    val numberOfMembers: Int,
    val currency: String
)

enum class Sort {
    TEXT,
    PRIORITY
}

enum class Order {
    ASC,
    DESC
}