package com.example.okanewari

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Enum defines the ROUTES for navigation
enum class OkaneWariScreen(){
    ListPartys,
    IndivParty
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
                        navController.navigate(OkaneWariScreen.IndivParty.name)
                    }
                )
            }
            composable(route = OkaneWariScreen.IndivParty.name) {
                PartyScreen(
                    onNextButtonClicked = {}
                )
            }
        }
    }
}
