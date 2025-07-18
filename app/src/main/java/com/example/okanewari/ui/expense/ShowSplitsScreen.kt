package com.example.okanewari.ui.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.data.MemberModel
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.PartyDetails
import java.math.BigDecimal

object ShowSplitsDestination: NavigationDestination {
    override val route = "show_splits"
    override val titleRes = R.string.show_splits_screen
    const val partyIdArg = "partyIdArg"
    val routeWithArg = "$route/{$partyIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSplitsScreen(
    navigateUp: () -> Unit,
    navigateBack: () -> Unit,
    canNavigateBackBool: Boolean = true,
    viewModel: ShowSplitsViewModel = viewModel(factory = OwViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = "Splits: ${viewModel.showSplitsUiState.topBarExpenseName}",
                canNavigateBack = canNavigateBackBool,
                navigateUp = navigateUp
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            TableScreen(
                partyDetails = viewModel.showSplitsUiState.partyUiState.partyDetails,
                memberList = viewModel.showSplitsUiState.memberList,
                memberSplitTotals = viewModel.showSplitsUiState.memberSplitTotals
            )
        }
    }
}

@Composable
fun TableScreen(
    partyDetails: PartyDetails,
    memberList: Map<Long, MemberModel>,
    memberSplitTotals: Map<Long, BigDecimal>
) {
    // Each cell of a column must have the same weight.
    val column1Weight = .5f // 50%
    val column2Weight = .5f // 50%
    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.medium_padding))
    ) {
        // Here is the header
        item {
            Row(
                modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                // Column 1 heading - Member Name
                TableCell(
                    text = stringResource(R.string.member_name),
                    style = MaterialTheme.typography.titleMedium,
                    weight = column1Weight
                )
                // Column 2 heading - Net total ($)
                TableCell(
                    text = "${stringResource(R.string.net_total_column)} (in ${partyDetails.currency})",
                    style = MaterialTheme.typography.titleMedium,
                    weight = column2Weight
                )
            }
        }

        // Here are all the lines of your table.
        items(memberSplitTotals.keys.toList()) { memId ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Column 1 - Member name
                TableCell(
                    text = memberList[memId]!!.name,
                    weight = column1Weight
                )
                // Column 2 - Net Total
                TableCell(
                    text = memberSplitTotals[memId].toString(),
                    weight = column2Weight
                )
            }
        }
        item{
            Text(
                text = "If the net total is POSITIVE, you ARE OWED money.\n" +
                        "If the net total is NEGATIVE, you OWE money.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Text(
        text = text,
        style = style,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.outline)
            .weight(weight)
            .padding(dimensionResource(R.dimen.small_padding))
    )
}