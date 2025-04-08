package com.example.okanewari.ui.party

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.components.DisplayFab
import com.example.okanewari.ui.components.DoneAndCancelButtons
import com.example.okanewari.ui.components.FabSize

object EditPartyDestination : NavigationDestination {
    override val route = "party_edit"
    override val titleRes = R.string.edit_party_details
    const val itemIdArg = "partyId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPartyScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    canNavigateBackBool: Boolean = true,
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = stringResource(EditPartyDestination.titleRes),
                canNavigateBack = canNavigateBackBool,
                navigateUp = navigateUp
            )
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxHeight(0.6f)
            ) {
                Text(
                    text = "nothing\nhere",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            DoneCancelDeleteButtons(
                doneButtonClick = navigateBack,
                cancelButtonClick = navigateBack,
                deleteButtonClicked = { }
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

    val mediumPadding = dimensionResource(R.dimen.medium_padding)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(mediumPadding),
        verticalArrangement = Arrangement.spacedBy(mediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledTonalButton(
            onClick = deleteButtonClicked,
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
    }
}