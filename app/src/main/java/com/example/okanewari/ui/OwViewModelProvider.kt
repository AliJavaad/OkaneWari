package com.example.okanewari.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.okanewari.ui.party.AddPartyViewModel
import com.example.okanewari.ui.party.ListPartysViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object OwViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ListPartysViewModel
        initializer {
            ListPartysViewModel()
        }
        // Initializer for AddPartyViewModel
        initializer {
            AddPartyViewModel()
        }
    }
}