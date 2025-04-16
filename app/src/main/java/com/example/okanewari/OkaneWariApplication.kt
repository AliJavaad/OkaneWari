package com.example.okanewari

import android.app.Application
import com.example.okanewari.data.AppContainer
import com.example.okanewari.data.AppDataContainer

/**
 * TODO
 * - Unit testing
 * - add a date modified entry to the party and entry tables
 *   - sort entries by the date modified
 * - input sanitization on the party name and other entry fields
 */

/**
 * Initializes app-wide dependencies (like databases, repositories, etc.)
 * that should exist for the entire app lifecycle.
 */
class OkaneWariApplication: Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}