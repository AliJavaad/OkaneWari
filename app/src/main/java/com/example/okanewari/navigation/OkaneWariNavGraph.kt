package com.example.okanewari.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.okanewari.ui.AddExpenseDestination
import com.example.okanewari.ui.AddExpenseScreen
import com.example.okanewari.ui.AddPartyDestination
import com.example.okanewari.ui.AddPartyScreen
import com.example.okanewari.ui.ListExpensesDestination
import com.example.okanewari.ui.ListPartiesDestination
import com.example.okanewari.ui.ListPartysScreen
import com.example.okanewari.ui.ListExpensesScreen

@Composable
fun OkaneWariNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        // Initialize the navController to the party list view
        navController = navController,
        startDestination = ListPartiesDestination.route,
        modifier = Modifier
    ){
        // Setting up navigation routes for each enum defined in OkaneWariScreen
        composable(route = ListPartiesDestination.route) {
            ListPartysScreen(
                // TODO pass in the party selected
                onPartyCardClicked = {
                    navController.navigate(ListExpensesDestination.route)
                },
                onAddPartyButtonClicked = {
                    navController.navigate(AddPartyDestination.route)
                }
            )
        }
        composable(route = AddPartyDestination.route){
            AddPartyScreen(
                onDoneButtonClicked = {},
                onCancelButtonClicked = {}
            )
        }
        composable(route = ListExpensesDestination.route) {
            ListExpensesScreen(
                onAddExpenseButtonClicked = {
                    navController.navigate(AddExpenseDestination.route)
                }
            )
        }
        composable(route = AddExpenseDestination.route) {
            AddExpenseScreen(
                onDoneButtonClicked = {},
                onCancelButtonClicked = {}
            )
        }
    }
}