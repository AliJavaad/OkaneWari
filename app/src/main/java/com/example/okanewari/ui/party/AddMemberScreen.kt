package com.example.okanewari.ui.party

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.components.DoneAndCancelButtons


object AddMemberDestination: NavigationDestination {
    override val route = "add_member"
    override val titleRes = R.string.add_new_member_screen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    canNavigateBackBool: Boolean = true
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = stringResource(AddMemberDestination.titleRes),
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
            DoneAndCancelButtons(
                doneButtonClick = {},
                cancelButtonClick = {},
                enableDone = true
            )

        }
    }
}

@Composable
fun MemberInputDialogue(
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    OutlinedTextField(
        value = "",
        onValueChange = {  },
        label = { Text(stringResource(R.string.member_name)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.medium_padding))
    )
}