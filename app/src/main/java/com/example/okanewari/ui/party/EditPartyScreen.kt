package com.example.okanewari.ui.party

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.data.MemberModel
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.DoneAndCancelButtons
import com.example.okanewari.ui.components.DoneCancelDeleteButtons
import com.example.okanewari.ui.components.PartyDetails
import kotlinx.coroutines.launch

object EditPartyDestination : NavigationDestination {
    override val route = "party_edit"
    override val titleRes = R.string.edit_party_details
    const val partyIdArg = "partyIdArg"
    val routeWithArgs = "$route/{$partyIdArg}"
}


/**
 * onEditMemberClicked: (List<Int>)
 *      [0] = party id
 *      [1] = member id
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPartyScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    navigateHome: () -> Unit,
    onEditMemberClicked: (List<Long>) -> Unit,
    onAddMemberClicked: (Long) -> Unit,
    canNavigateBackBool: Boolean = true,
    modifier: Modifier = Modifier,
    viewModel: EditPartyViewModel = viewModel(factory = OwViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = "Editing: ${viewModel.editPartyUiState.topBarPartyName}",
                canNavigateBack = canNavigateBackBool,
                navigateUp = navigateUp
            )
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            PartyInputForm(
                partyDetails = viewModel.editPartyUiState.partyUiState.partyDetails,
                onValueChange = viewModel::updatePartyUiState,
            )
            HorizontalDivider(
                thickness = 4.dp,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.medium_padding))
            )
            Text(
                text = "Add / Modify members:",
                modifier = Modifier.padding(all  = dimensionResource(R.dimen.medium_padding))
            )
            ListPartyMembers(
                membersList = viewModel.editPartyUiState.memberList,
                partyDetails = viewModel.editPartyUiState.partyUiState.partyDetails,
                onModifyMemberClicked = onEditMemberClicked,
                onAddMemberClicked = onAddMemberClicked
            )
            HorizontalDivider(
                thickness = 4.dp,
                modifier = Modifier.padding(all = dimensionResource(R.dimen.medium_padding))
            )
            DoneCancelDeleteButtons(
                doneButtonClick = { coroutineScope.launch{
                    viewModel.updateParty()
                    navigateBack()
                } },
                cancelButtonClick = navigateBack,
                deleteButtonClicked = { coroutineScope.launch{
                    viewModel.deleteParty()
                    navigateHome()
                } },
                enableDone = viewModel.editPartyUiState.partyUiState.isEntryValid
            )
        }
    }
}

@Composable
fun ListPartyMembers(
    membersList: List<MemberModel>,
    partyDetails: PartyDetails,
    onModifyMemberClicked: (List<Long>) -> Unit,
    onAddMemberClicked: (Long) -> Unit
){
    val mediumPadding = dimensionResource(R.dimen.medium_padding)
    for (member in membersList){
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(mediumPadding)
                .clickable {
                    Log.d("Member Card", "Member Card Clicked: ${member.id}")
                    onModifyMemberClicked(listOf(partyDetails.id, member.id))
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = "Photo of a person",
                    // TODO move constants to resource folder
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.small_thumbnail_width))
                        .height(dimensionResource(R.dimen.small_thumbnail_height))
                        .padding(all = mediumPadding)
                )
                Text(
                    text = member.name,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
    FilledTonalButton(
        modifier = Modifier.padding(mediumPadding),
        onClick = { onAddMemberClicked(partyDetails.id) }
    ) {
        Text(
            text = stringResource(R.string.add_new_member),
            fontSize = 16.sp
        )
    }
}


