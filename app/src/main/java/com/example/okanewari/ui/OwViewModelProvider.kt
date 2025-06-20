package com.example.okanewari.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.okanewari.OkaneWariApplication
import com.example.okanewari.ui.expense.ListExpensesViewModel
import com.example.okanewari.ui.expense.AddExpenseViewModel
import com.example.okanewari.ui.expense.EditExpenseViewModel
import com.example.okanewari.ui.party.AddPartyViewModel
import com.example.okanewari.ui.party.EditPartyViewModel
import com.example.okanewari.ui.party.ListPartysViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire app
 */
object OwViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ListPartysViewModel
        initializer {
            ListPartysViewModel(okaneWariApplication().container.okaneWariRepository)
        }
        // Initializer for AddPartyViewModel:
        /**
         * okaneWariApplication() is called to get the OkaneWariApplication instance,
         * then its container provides the partyRepository
         */
        initializer {
            AddPartyViewModel(okaneWariApplication().container.okaneWariRepository)
        }
        // Initializer for ListExpensesViewModel
        initializer {
            ListExpensesViewModel(
                this.createSavedStateHandle(),
                okaneWariApplication().container.okaneWariRepository
            )
        }
        // Initializer for EditPartyViewModel
        initializer {
            EditPartyViewModel(
                this.createSavedStateHandle(),
                okaneWariApplication().container.okaneWariRepository
            )
        }
        // Initializer for AddExpenseViewModel
        initializer {
            AddExpenseViewModel(
                this.createSavedStateHandle(),
                okaneWariApplication().container.okaneWariRepository
            )
        }
        // Initializer for EditExpenseViewModel
        initializer {
            EditExpenseViewModel(
                this.createSavedStateHandle(),
                okaneWariApplication().container.okaneWariRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [OkaneWariApplication].
 * [AndroidViewModelFactory.APPLICATION_KEY] A predefined key to fetch the Application object from
 * CreationExtras.
 */
fun CreationExtras.okaneWariApplication(): OkaneWariApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as OkaneWariApplication)