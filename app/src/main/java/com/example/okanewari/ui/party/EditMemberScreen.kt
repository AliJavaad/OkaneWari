package com.example.okanewari.ui.party

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination


object EditMemberDestination: NavigationDestination {
    override val route = "edit_member"
    override val titleRes = R.string.edit_member_screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMemberScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    canNavigateBackBool: Boolean = true
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = stringResource(EditMemberDestination.titleRes),
                canNavigateBack = canNavigateBackBool,
                navigateUp = navigateUp
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            MemberInputDialogue(
                contentPadding = innerPadding
            )
            DoneCancelDeleteButtons(
                doneButtonClick = {},
                cancelButtonClick = {},
                deleteButtonClicked = {},
                enableDone = true
            )
        }
    }
}
