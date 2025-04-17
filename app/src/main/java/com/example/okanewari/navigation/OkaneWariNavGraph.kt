package com.example.okanewari.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.okanewari.ui.expense.AddExpenseDestination
import com.example.okanewari.ui.expense.AddExpenseScreen
import com.example.okanewari.ui.expense.EditExpenseDestination
import com.example.okanewari.ui.expense.EditExpenseScreen
import com.example.okanewari.ui.party.AddPartyDestination
import com.example.okanewari.ui.party.AddPartyScreen
import com.example.okanewari.ui.expense.ListExpensesDestination
import com.example.okanewari.ui.party.ListPartiesDestination
import com.example.okanewari.ui.party.ListPartysScreen
import com.example.okanewari.ui.expense.ListExpensesScreen
import com.example.okanewari.ui.party.EditPartyDestination
import com.example.okanewari.ui.party.EditPartyScreen

/**
 * Holds the navigation for the app.
 */
@Composable
fun OkaneWariNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        // Initialize the navController to the party list view
        navController = navController,
        startDestination = ListPartiesDestination.route,
        modifier = Modifier.fillMaxSize()
    ){
        // Setting up navigation routes for each enum defined in OkaneWariScreen
        composable(route = ListPartiesDestination.route) {
            ListPartysScreen(
                onPartyCardClicked = {
                    navController.navigate("${ListExpensesDestination.route}/${it}") },
                onAddPartyButtonClicked = { navController.navigate(AddPartyDestination.route) }
            )
        }
        composable(route = AddPartyDestination.route){
            AddPartyScreen(
                navigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = EditPartyDestination.routeWithArgs,
            arguments = listOf(navArgument(EditPartyDestination.partyIdArg){
                type = NavType.IntType})
        ){
            EditPartyScreen(
                navigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() },
                navigateHome = {
                    navController.navigate(ListPartiesDestination.route){
                        // Pop everything in the navigation stack
                        popUpTo(0){
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = ListExpensesDestination.routeWithArg,
            arguments = listOf(navArgument(ListExpensesDestination.partyIdArg){
                type = NavType.IntType})
        ) {
            ListExpensesScreen(
                navigateUp = { navController.navigateUp() },
                onAddExpenseButtonClicked = {
                    navController.navigate("${AddExpenseDestination.route}/${it}") },
                onSettingsButtonClicked = {
                    navController.navigate("${EditPartyDestination.route}/${it}") },
                onExpenseCardClick = { navController.navigate(EditExpenseDestination.route) }
            )
        }
        composable(
            route = AddExpenseDestination.routeWithArg,
            arguments = listOf(navArgument(AddExpenseDestination.partyIdArg){
                type = NavType.IntType })
        ) {
            AddExpenseScreen(
                navigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(route = EditExpenseDestination.route) {
            EditExpenseScreen(
                navigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}