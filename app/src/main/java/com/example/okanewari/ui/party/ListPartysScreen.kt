package com.example.okanewari.ui.party

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.data.PartyModel
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.ConfirmationDialog
import com.example.okanewari.ui.components.DateHandler
import com.example.okanewari.ui.components.DisplayFab
import com.example.okanewari.ui.components.FabSize

object ListPartiesDestination : NavigationDestination {
    override val route = "list_parties"
    override val titleRes = R.string.list_parties
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPartysScreen(
    onPartyCardClicked: (Long) -> Unit,
    onAddPartyButtonClicked: () -> Unit,
    canNavigateBackBool: Boolean = false,
    modifier: Modifier = Modifier,
    viewModel: ListPartysViewModel = viewModel(factory = OwViewModelProvider.Factory)
){

    val listPartysUiState by viewModel.listPartysUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var checkPartyLimit by rememberSaveable { mutableStateOf(false) }

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            OkaneWareTopAppBar(
                title = stringResource(ListPartiesDestination.titleRes),
                canNavigateBack = canNavigateBackBool,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            DisplayFab(
                myClick = { checkPartyLimit = true },
                fabSize = FabSize.LARGE
            )
        }
    ){ innerPadding ->
        ListPartysBody(
            partyList = listPartysUiState.partyList,
            partyClicked = onPartyCardClicked,
            contentPadding = innerPadding
        )
        if(checkPartyLimit){
            if (viewModel.isOverPartyCountLimit(listPartysUiState.partyList.size)){
                ConfirmationDialog(
                    onConfirm = { checkPartyLimit = false },
                    onCancel = { checkPartyLimit = false },
                    title = stringResource(R.string.attention),
                    text = "Party limit reached.",
                    confirmText = stringResource(R.string.ok),
                    showDismissButton = false
                )
            }else{
                checkPartyLimit = false
                onAddPartyButtonClicked()
            }
        }
    }
}

@Composable
fun ListPartysBody(
    partyList: List<PartyModel>,
    partyClicked: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (partyList.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxHeight(0.6f)
            ) {
                Text(
                    text = stringResource(R.string.empty_party_list),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxWidth()
                )
            }
        } else{
            DisplayParties(
                partyList = partyList,
                partyClicked = partyClicked,
                contentPadding = contentPadding,
                modifier = modifier
            )
        }
    }
}

@Composable
fun DisplayParties(
    partyList: List<PartyModel>,
    partyClicked: (Long) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.padding(contentPadding)
    ) {
        items(partyList) {
            Card (
                // Making each card a clickable to take to the party screen
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.medium_padding))
                    .clickable {
                        Log.d("PartyKey", "Inserting selected party's key: ${it.id}")
                        partyClicked(it.id)
                    }
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = painterResource(id = R.drawable.baseline_groups_24),
                        contentDescription = "Photo of a person",
                        // TODO move constants to resource folder
                        modifier = Modifier
                            .width(dimensionResource(R.dimen.thumbnail_width))
                            .height(dimensionResource(R.dimen.thumbnail_height))
                            .padding(all = dimensionResource(R.dimen.medium_padding))
                    )
                    Column{
                        Text(
                            text = it.partyName,
                            style = MaterialTheme.typography.headlineMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = stringResource(R.string.last_modified) + ": " +
                                    DateHandler.formatter.format(it.dateModded),
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
