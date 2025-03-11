package com.example.okanewari

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.okanewari.ui.AddExpenseScreen
import com.example.okanewari.ui.AddPartyScreen
import com.example.okanewari.ui.ListPartysScreen
import com.example.okanewari.ui.OkaneViewModel
import com.example.okanewari.ui.PartyScreen

// Enum defines the ROUTES for navigation
enum class OkaneWariScreen(val title: Int){
    ListPartys(title = R.string.home_screen),
    ShowParty(title = R.string.my_party),
    AddParty(title = R.string.add_party),
    AddExpense(title = R.string.add_expense)
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OkaneWariAppBar(
    currentScreen: OkaneWariScreen,
    currParty: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Check if the currentScreen is on a 'party screen' and set name accordingly
    var title = stringResource(currentScreen.title)
    val showPartyName = stringResource(OkaneWariScreen.ShowParty.title)
    if (title == showPartyName){
        title = currParty
    }
    TopAppBar(
        // TODO change the title depending on the party name
        title = { Text(title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}


@Composable
fun OkaneWariApp(
    owViewModel: OkaneViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
){
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = OkaneWariScreen.valueOf(
        backStackEntry?.destination?.route ?: OkaneWariScreen.ListPartys.name
    )
    val uiState by owViewModel.uiState.collectAsState()

    // The view
    Scaffold(

        // Creating the top app bar
        topBar = {
            OkaneWariAppBar(
                currentScreen = currentScreen,
                currParty = uiState.currentPartyName,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->

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
                        owViewModel.setCurrentPartyName(it)
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
