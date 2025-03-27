package com.example.okanewari

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
import androidx.compose.material3.TopAppBarScrollBehavior
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
import com.example.okanewari.navigation.OkaneWariNavHost
import com.example.okanewari.ui.AddExpenseScreen
import com.example.okanewari.ui.AddPartyScreen
import com.example.okanewari.ui.ListPartysScreen
import com.example.okanewari.ui.OkaneViewModel
import com.example.okanewari.ui.ListExpensesScreen

//// Enum defines the ROUTES for navigation
//enum class OkaneWariScreen(val title: Int){
//    ListPartys(title = R.string.my_parties),
//    ShowParty(title = R.string.party_name),
//    AddParty(title = R.string.add_new_party),
//    AddExpense(title = R.string.add_new_expense)
//}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OkaneWareTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    TopAppBar(title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        })
}

//fun OkaneWariAppBar(
//    currentScreen: OkaneWariScreen,
//    currParty: String,
//    canNavigateBack: Boolean,
//    navigateUp: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    // Check if the currentScreen is on a 'party screen' and set name accordingly
//    var title = stringResource(currentScreen.title)
//    val showPartyName = stringResource(OkaneWariScreen.ShowParty.title)
//    if (title == showPartyName){
//        title = currParty
//    }
//    TopAppBar(
//        // TODO change the title depending on the party name
//        title = { Text(title) },
//        colors = TopAppBarDefaults.mediumTopAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer
//        ),
//        modifier = modifier,
//        navigationIcon = {
//            if (canNavigateBack) {
//                IconButton(onClick = navigateUp) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = stringResource(R.string.back_button)
//                    )
//                }
//            }
//        }
//    )
//}


@Composable
fun OkaneWariApp(
    owViewModel: OkaneViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
){
    val uiState by owViewModel.uiState.collectAsState()
    OkaneWariNavHost(navController = navController)
}
