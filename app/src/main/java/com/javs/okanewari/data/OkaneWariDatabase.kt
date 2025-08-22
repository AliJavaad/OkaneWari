package com.javs.okanewari.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PartyModel::class, ExpenseModel::class, MemberModel::class, SplitModel::class],
    version = 10,
    // TODO exportSchema
    exportSchema = false
)
abstract class OkaneWariDatabase: RoomDatabase() {
    abstract fun partyDao(): PartyDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun memberDao(): MemberDao
    abstract fun splitDao(): SplitDao

    companion object {
        // Instance helps maintain a single instance of the database opened at a given time
        @Volatile
        private var Instance: OkaneWariDatabase? = null

        fun getDatabase(context: Context): OkaneWariDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            // synchronized{} block avoids race condition.
            return Instance ?: synchronized(this) {
                // TODO Migration handler
                Room.databaseBuilder(context, OkaneWariDatabase::class.java, "ow_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }

}