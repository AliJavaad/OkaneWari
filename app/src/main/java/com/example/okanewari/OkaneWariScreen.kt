package com.example.okanewari

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.okanewari.ui.AddExpenseScreen
import com.example.okanewari.ui.AddPartyScreen
import com.example.okanewari.ui.ListPartysScreen
import com.example.okanewari.ui.PartyScreen

// Enum defines the ROUTES for navigation
enum class OkaneWariScreen(){
    ListPartys,
    ShowParty,
    AddParty,
    AddExpense
}

@Composable
fun OkaneWariApp(
    navController: NavHostController = rememberNavController()
){
    // The view
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            // Initialize the navController to the party list view
            navController = navController,
            startDestination = OkaneWariScreen.ListPartys.name,
            modifier = Modifier.padding(innerPadding)
        ){
            // Setting up navigation routes for each enum defined in OkaneWariScreen
            composable(route = OkaneWariScreen.ListPartys.name) {
                ListPartysScreen(
                    // TODO pass in the party selected
                    onPartyCardClicked = {
                        // get the party data
                        navController.navigate(OkaneWariScreen.ShowParty.name)
                    },
                    onAddPartyButtonClicked = {
                        navController.navigate(OkaneWariScreen.AddParty.name)
                    }
                )
            }
            composable(route = OkaneWariScreen.AddParty.name){
                AddPartyScreen(
                    onDoneButtonClicked = {},
                    onCancelButtonClicked = {}
                )
            }
            composable(route = OkaneWariScreen.ShowParty.name) {
                PartyScreen(
                    onAddExpenseButtonClicked = {
                        navController.navigate(OkaneWariScreen.AddExpense.name)
                    }
                )
            }
            composable(route = OkaneWariScreen.AddExpense.name) {
                AddExpenseScreen(
                    onDoneButtonClicked = {},
                    onCancelButtonClicked = {}
                )
            }
        }
    }
}
