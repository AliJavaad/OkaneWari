package com.example.okanewari.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val partyRepository: PartyRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    /**
     * Implementation for [PartyRepository]
     */
    override val partyRepository: PartyRepository by lazy {
        OfflinePartyRepository(
            OkaneWariDatabase.getDatabase(context).partyDao(),
            OkaneWariDatabase.getDatabase(context).expenseDao()
        )
    }
}