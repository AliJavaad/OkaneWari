package com.example.okanewari.ui.party

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.Party
import com.example.okanewari.R
import com.example.okanewari.data.DummyPartyData
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.components.DisplayFab
import com.example.okanewari.ui.components.FabSize

object ListPartiesDestination : NavigationDestination {
    override val route = "list_parties"
    override val titleRes = R.string.list_parties
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPartysScreen(
    onPartyCardClicked: (String) -> Unit,
    onAddPartyButtonClicked: () -> Unit,
    canNavigateBackBool: Boolean = false,
    modifier: Modifier = Modifier
){
    // TODO Get info passed by click
    val myPartys = DummyPartyData()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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
                myClick = onAddPartyButtonClicked,
                fabSize = FabSize.LARGE
            )
        }
    ){ innerPadding ->
        DisplayParties(
            parties = myPartys,
            myClick = onPartyCardClicked,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun DisplayParties(
    parties: List<Party>,
    myClick: (String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = Modifier.padding(contentPadding)
    ) {
        // TODO case where party list is empty
        items(parties) {
            Card (
                // Making each card a clickable to take to the party screen
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.medium_padding))
                    .clickable(onClick = { myClick(it.name) },)
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
                            // TODO align text in the column
                            // TODO move constants to resource folder
                            text = it.name,
                            fontSize = 32.sp,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
