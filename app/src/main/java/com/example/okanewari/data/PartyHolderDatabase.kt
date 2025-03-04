package com.example.okanewari.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PartyHolderModel::class], version = 1)
abstract class PartyHolderDatabase: RoomDatabase() {
    abstract val dao: PartyHolderDao
}