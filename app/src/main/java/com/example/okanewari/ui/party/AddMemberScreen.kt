package com.example.okanewari.ui.party

import androidx.compose.foundation.layout.Column
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.DoneAndCancelButtons
import com.example.okanewari.ui.components.MemberDetails
import com.example.okanewari.ui.components.PartyDetails
import kotlinx.coroutines.launch
import java.util.Date


object AddMemberDestination: NavigationDestination {
    override val route = "add_member"
    override val titleRes = R.string.add_new_member_screen
    const val partyIdArg = "partyIdArg"
    val routeWithArgs = "${route}/{$partyIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    canNavigateBackBool: Boolean = true,
    viewModel: AddMemberViewModel = viewModel(factory = OwViewModelProvider.Factory)
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
                memberDetails = viewModel.addMemberUiState.memberUiState.memberDetails,
                partyDetails = viewModel.addMemberUiState.partyUiState.partyDetails,
                onValueChange = viewModel::updateUiState
            )
            DoneAndCancelButtons(
                doneButtonClick = {
                    coroutineScope.launch{
                        viewModel.saveMember()
                        viewModel.updateParty()
                        navigateBack()
                    }
                },
                cancelButtonClick = navigateBack,
                enableDone = viewModel.addMemberUiState.memberUiState.isEntryValid
            )

        }
    }
}

@Composable
fun MemberInputDialogue(
    memberDetails: MemberDetails,
    partyDetails: PartyDetails,
    onValueChange: (PartyDetails, MemberDetails) -> Unit
){
    OutlinedTextField(
        value = memberDetails.name,
        onValueChange = { onValueChange(partyDetails.copy(dateModded = Date()), memberDetails.copy(name = it)) },
        label = { Text(stringResource(R.string.member_name)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.medium_padding))
    )
}