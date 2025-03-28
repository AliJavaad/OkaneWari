package com.example.okanewari.ui.party

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.DoneAndCancelButtons


object AddPartyDestination: NavigationDestination{
    override val route = "add_new_party"
    override val titleRes = R.string.add_new_party
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPartyScreen(
    onDoneButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    canNavigateBackBool: Boolean = true,
    viewModel: AddPartyViewModel = viewModel(factory = OwViewModelProvider.Factory)
){
    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = stringResource(AddPartyDestination.titleRes),
                canNavigateBack = canNavigateBackBool,
                navigateUp = onCancelButtonClicked
            )
        }
    ){ innerPadding ->
        // TODO add in save coroutine
        AddPartyBody(
            partyUiState = viewModel.partyUiState,
            onValueChange = viewModel::updateUiState,
            onDone = onDoneButtonClicked,
            onCancel = onCancelButtonClicked,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun AddPartyBody(
    partyUiState: PartyUiState,
    onValueChange: (PartyDetails) -> Unit,
    onDone: () -> Unit,
    onCancel: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){
    Column(
        modifier = Modifier.padding(contentPadding)
    ){
        PartyInputForm(
            partyDetails = partyUiState.partyDetails,
            onValueChange = onValueChange,
        )
        DoneAndCancelButtons(
            doneButtonClick = onDone,
            cancelButtonClick = onCancel
        )
    }
}

@Composable
fun PartyInputForm(
    partyDetails: PartyDetails,
    onValueChange: (PartyDetails) -> Unit = {},
    modifier: Modifier = Modifier,
){
    TextField(
        value = partyDetails.partyName,
        onValueChange = { onValueChange(partyDetails.copy(partyName = it)) },
        label = { Text(stringResource(R.string.party_name)) },
        placeholder = { Text(stringResource(R.string.my_party)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(R.dimen.medium_padding))
    )
}