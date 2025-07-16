package com.example.okanewari

import android.app.Application
import com.example.okanewari.data.AppContainer
import com.example.okanewari.data.AppDataContainer

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