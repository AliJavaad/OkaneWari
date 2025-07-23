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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.DoneCancelDeleteButtons
import kotlinx.coroutines.launch


object EditMemberDestination: NavigationDestination {
    override val route = "edit_member"
    override val titleRes = R.string.edit_member_screen
    const val partyIdArg = "partyIdArg"
    const val memberIdArg = "expenseIdArg"
    val routeWithArg = "${route}/{$partyIdArg}/{$memberIdArg}"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMemberScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    canNavigateBackBool: Boolean = true,
    viewModel: EditMemberViewModel = viewModel(factory = OwViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = stringResource(R.string.editing_member) + ": " +
                        viewModel.editMemberUiState.topBarPartyName,
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
                memberDetails = viewModel.editMemberUiState.memberUiState.memberDetails,
                partyDetails = viewModel.editMemberUiState.partyUiState.partyDetails,
                onValueChange = viewModel::updateUiState
            )
            DoneCancelDeleteButtons(
                doneButtonClick = {
                    coroutineScope.launch {
                        viewModel.updateParty()
                        viewModel.updateMember()
                        navigateBack()
                    }
                },
                cancelButtonClick = navigateBack,
                deleteButtonClicked = {
                    coroutineScope.launch {
                        viewModel.deleteMember()
                        navigateBack()
                    }
                },
                enableDone = viewModel.editMemberUiState.memberUiState.isEntryValid,
                dialogueText = stringResource(R.string.delete_member_dialogue_text_simple)
            )
        }
    }
}