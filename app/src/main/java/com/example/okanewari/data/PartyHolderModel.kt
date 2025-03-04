package com.example.okanewari.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
NOTES:

Party List table reqs.
    Key:
    PartyName: Parties should be able to have the same name
    EntryTableKey: Key to the entry table

Entry list table
    Entry number: (key)
    Name of Expense:
    Total Amount:
    Date: WAL
    Member_1: (Amount paid by member x)
    Member_2:
    Member_3: (Creating a new member would create a new column in the table)

    max member number = 15?
    max entries?
 */

@Entity
data class PartyHolderModel (
    /*
    id: Unique key so partys can have same name
    partyName: party name that should be displayed
    entryTableName: name of the table that corresponds to this party
                new entry table name: $partyName_$key
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val partyName: String,
    val entryTableName: String = ""
)

enum class Sort {
    TEXT,
    PRIORITY
}

enum class Order {
    ASC,
    DESC
}