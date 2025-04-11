package com.example.okanewari.ui.party

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.DoneAndCancelButtons
import kotlinx.coroutines.launch

object EditPartyDestination : NavigationDestination {
    override val route = "party_edit"
    override val titleRes = R.string.edit_party_details
    const val partyIdArg = "partyIdArg"
    val routeWithArgs = "$route/{$partyIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPartyScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    navigateHome: () -> Unit,
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
            modifier = Modifier.padding(innerPadding)
        ) {
            PartyInputForm(
                partyDetails = viewModel.editPartyUiState.partyUiState.partyDetails,
                currencyDropdown = viewModel.editPartyUiState.currencyDropdown,
                onValueChange = viewModel::updateUiState,
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
                } }
            )
        }
    }
}

@Composable
fun DoneCancelDeleteButtons(
    doneButtonClick: () -> Unit,
    cancelButtonClick: () -> Unit,
    deleteButtonClicked: () -> Unit,
    enableDone: Boolean = true
) {
    DoneAndCancelButtons(
        doneButtonClick = doneButtonClick,
        cancelButtonClick = cancelButtonClick
    )

    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    val mediumPadding = dimensionResource(R.dimen.medium_padding)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(mediumPadding),
        verticalArrangement = Arrangement.spacedBy(mediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledTonalButton(
            onClick = { deleteConfirmationRequired = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
            ){
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
                Text(
                    text = stringResource(R.string.delete),
                    fontSize = 16.sp
                )
            }
        }
        if (deleteConfirmationRequired){
            DeleteConfirmationDialog(
                onConfirm = {
                    deleteConfirmationRequired = false
                    deleteButtonClicked() },
                onCancel = { deleteConfirmationRequired = false }
            )
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}
