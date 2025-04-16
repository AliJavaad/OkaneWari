package com.example.okanewari.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val partyRepository: OkaneWariRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    /**
     * Implementation for [OkaneWariRepository]
     */
    override val partyRepository: OkaneWariRepository by lazy {
        OfflineOkaneWariRepository(
            OkaneWariDatabase.getDatabase(context).partyDao(),
            OkaneWariDatabase.getDatabase(context).expenseDao()
        )
    }
}