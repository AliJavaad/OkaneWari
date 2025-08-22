package com.javs.okanewari.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.javs.okanewari.ui.expense.AddExpenseDestination
import com.javs.okanewari.ui.expense.AddExpenseScreen
import com.javs.okanewari.ui.expense.EditExpenseDestination
import com.javs.okanewari.ui.expense.EditExpenseScreen
import com.javs.okanewari.ui.party.AddPartyDestination
import com.javs.okanewari.ui.party.AddPartyScreen
import com.javs.okanewari.ui.expense.ListExpensesDestination
import com.javs.okanewari.ui.party.ListPartiesDestination
import com.javs.okanewari.ui.party.ListPartysScreen
import com.javs.okanewari.ui.expense.ListExpensesScreen
import com.javs.okanewari.ui.expense.ShowSplitsDestination
import com.javs.okanewari.ui.expense.ShowSplitsScreen
import com.javs.okanewari.ui.party.AddMemberDestination
import com.javs.okanewari.ui.party.AddMemberScreen
import com.javs.okanewari.ui.party.EditMemberDestination
import com.javs.okanewari.ui.party.EditMemberScreen
import com.javs.okanewari.ui.party.EditPartyDestination
import com.javs.okanewari.ui.party.EditPartyScreen

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
                onEditMemberClicked = {
                    navController.navigate("${EditMemberDestination.route}/${it[0]}/${it[1]}") },
                onAddMemberClicked = {
                    navController.navigate("${AddMemberDestination.route}/${it}") },
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
            route = AddMemberDestination.routeWithArgs,
            arguments = listOf(navArgument(AddMemberDestination.partyIdArg){
                type = NavType.IntType})
        ){
            AddMemberScreen(
                navigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = EditMemberDestination.routeWithArg,
            arguments = listOf(
                navArgument(EditMemberDestination.partyIdArg){ type = NavType.IntType},
                navArgument(EditMemberDestination.memberIdArg){ type = NavType.IntType}
            )
        ){
            EditMemberScreen(
                navigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() }
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
                onExpenseCardClick = {
                    navController.navigate("${EditExpenseDestination.route}/${it[0]}/${it[1]}") },
                onStatCardClicked = {
                    navController.navigate("${ShowSplitsDestination.route}/${it}")}
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
        composable(
            route = EditExpenseDestination.routeWithArg,
            arguments = listOf(
                navArgument(EditExpenseDestination.partyIdArg){ type = NavType.IntType },
                navArgument(EditExpenseDestination.expenseIdArg){ type = NavType.IntType }
                )
        ) {
            EditExpenseScreen(
                navigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = ShowSplitsDestination.routeWithArg,
            arguments = listOf(navArgument(ShowSplitsDestination.partyIdArg){
                type = NavType.IntType })
        ) {
            ShowSplitsScreen(
                navigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}