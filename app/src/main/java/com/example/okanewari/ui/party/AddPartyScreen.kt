package com.example.okanewari.ui.party

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.CurrencySymbols
import com.example.okanewari.ui.components.DoneAndCancelButtons
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import kotlinx.coroutines.launch
import java.util.Date


object AddPartyDestination: NavigationDestination{
    override val route = "add_new_party"
    override val titleRes = R.string.add_new_party
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPartyScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    canNavigateBackBool: Boolean = true,
    viewModel: AddPartyViewModel = viewModel(factory = OwViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = stringResource(AddPartyDestination.titleRes),
                canNavigateBack = canNavigateBackBool,
                navigateUp = navigateUp
            )
        }
    ){ innerPadding ->
        PartyEntryBody(
            partyUiState = viewModel.addPartyUiState.partyUiState,
            onPartyValueChange = viewModel::updateUiState,
            onDone = {
                coroutineScope.launch{
                    viewModel.savePartyAndHostMember()
                    navigateBack()
                }
            },
            onCancel = navigateBack,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun PartyEntryBody(
    partyUiState: PartyUiState,
    onPartyValueChange: (PartyDetails) -> Unit,
    onDone: () -> Unit,
    onCancel: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    Column(
        modifier = Modifier
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ){
        PartyInputForm(
            partyDetails = partyUiState.partyDetails,
            onValueChange = onPartyValueChange
        )
        HorizontalDivider(
            thickness = 4.dp,
            modifier = Modifier.padding(all = dimensionResource(R.dimen.medium_padding))
        )
        DoneAndCancelButtons(
            doneButtonClick = onDone,
            cancelButtonClick = onCancel,
            enableDone = partyUiState.isEntryValid
        )
    }
}

@Composable
fun PartyInputForm(
    partyDetails: PartyDetails,
    onValueChange: (PartyDetails) -> Unit,
    modifier: Modifier = Modifier
){
    var currencyDropdownMenu by rememberSaveable { mutableStateOf(false) }
    /**
     * Handling the party name input field
     */
    TextField(
        value = partyDetails.partyName,
        onValueChange = {
            onValueChange(partyDetails.copy(partyName = it, dateModded = Date())) },
        label = { Text(stringResource(R.string.party_name)) },
        placeholder = { Text(stringResource(R.string.my_party)) },
        supportingText = {Text(stringResource(R.string.name_format_warning))},
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(R.dimen.medium_padding))
    )
    /**
     * Handling the currency symbol input
     */
    Row (
        modifier = Modifier.padding(all = dimensionResource(R.dimen.medium_padding))
    ){
        Text(stringResource(R.string.select_a_currency) + ": ")
        Box(
            modifier = Modifier
                .width(dimensionResource(R.dimen.currency_symbol_spacing))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable { currencyDropdownMenu = true }
        ){
            Image(
                painter = painterResource(R.drawable.baseline_arrow_drop_down_24),
                contentDescription = "asdf",
                modifier = Modifier.align(Alignment.TopEnd)
            )
            Text(
                text = partyDetails.currency,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.small_padding))
            )
            DropdownMenu(
                expanded = currencyDropdownMenu,
                onDismissRequest = { currencyDropdownMenu = false }
            ) {
                for(currency in CurrencySymbols.dropdownCurrencyMenu){
                    DropdownMenuItem(
                        text = { Text(currency.symbol + "  (${currency.description})")},
                        onClick = {
                            onValueChange(partyDetails.copy(currency = currency.symbol))
                            currencyDropdownMenu = false
                        }
                    )
                }
            }
        }
    }
}
