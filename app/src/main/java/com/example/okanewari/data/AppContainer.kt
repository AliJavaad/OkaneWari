package com.example.okanewari.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val okaneWariRepository: OkaneWariRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    /**
     * Implementation for [OkaneWariRepository]
     */
    override val okaneWariRepository: OkaneWariRepository by lazy {
        OfflineOkaneWariRepository(
            OkaneWariDatabase.getDatabase(context).partyDao(),
            OkaneWariDatabase.getDatabase(context).expenseDao()
        )
    }
}