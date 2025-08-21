package com.example.okanewari.ui.expense

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
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
    val medPadding = dimensionResource(R.dimen.medium_padding)

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
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            ShowSimplifiedTransactions(
                memberList = viewModel.showSplitsUiState.memberList,
                transactions = viewModel.showSplitsUiState.transactions,
                partyDetails = viewModel.showSplitsUiState.partyUiState.partyDetails
            )
            HorizontalDivider(
                thickness = 4.dp,
                modifier = Modifier.padding(all = medPadding)
            )
            ShowNetSplitsTable(
                partyDetails = viewModel.showSplitsUiState.partyUiState.partyDetails,
                memberList = viewModel.showSplitsUiState.memberList,
                memberSplitTotals = viewModel.showSplitsUiState.memberSplitTotals
            )
            HorizontalDivider(
                thickness = 4.dp,
                modifier = Modifier.padding(all = medPadding)
            )
            ShowSettleUpButton(
                onConfirm = {
                    coroutineScope.launch {
                        try{
                            viewModel.deleteAllExpenses()
                            navigateBack()
                        }catch (e: Exception){
                            coroutineContext.ensureActive()
                            Log.e("ShowSplits", "Failed to delete all expenses", e)
                        }

                    }
                }
            )
        }
    }
}

@Composable
fun ShowSettleUpButton(
    onConfirm: () -> Unit,
    enableSettle: Boolean = true
){
    var settleConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.medium_padding)),
        enabled = enableSettle,
        onClick = { settleConfirmationRequired = true }
    ) {
        Text(
            text = "Settle Up",
            style = MaterialTheme.typography.labelLarge,
        )
    }

    if(settleConfirmationRequired){
        AlertDialog(
            onDismissRequest = { settleConfirmationRequired = false },
            title = { Text(stringResource(R.string.attention)) },
            text = { Text("By settling up, all expenses will be DELETED (reset).\n\nHowever, all party and member details will remain.") },
            dismissButton = {
                TextButton(
                    onClick = { settleConfirmationRequired = false }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        settleConfirmationRequired = false
                        onConfirm()
                    }
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        )
    }
}

@Composable
fun ShowSimplifiedTransactions(
    memberList: Map<Long, MemberModel>,
    transactions: List<Transaction>,
    partyDetails: PartyDetails
){
    val medPadding = dimensionResource(R.dimen.medium_padding)
    Text(
        text = "Splits:",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = medPadding)
    )
    if(transactions.isEmpty()){
        Text(
            text = "No splits to show.",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(horizontal = medPadding * 2)
                .padding(vertical = dimensionResource(R.dimen.small_padding))
        )
        return
    }
    transactions.forEach{ trans ->
        val debtorName = memberList[trans.fromKey]?.name
        val creditorName = memberList[trans.toKey]?.name

        Text(
            text = "$debtorName owes $creditorName ${partyDetails.currency}${trans.amount}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(horizontal = medPadding * 2)
                .padding(vertical = dimensionResource(R.dimen.small_padding))
        )
    }
}

@Composable
fun ShowNetSplitsTable(
    partyDetails: PartyDetails,
    memberList: Map<Long, MemberModel>,
    memberSplitTotals: Map<Long, BigDecimal>
) {
    val medPadding = dimensionResource(R.dimen.medium_padding)
    // Each cell of a column must have the same weight.
    val column1Weight = .5f // 50%
    val column2Weight = .5f // 50%

    Row(
        modifier = Modifier
            .padding(horizontal = medPadding)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
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

    // All data lines of your table.
    memberSplitTotals.keys.forEach { memId ->
        Row(
            modifier = Modifier
                .padding(horizontal = medPadding)
                .fillMaxWidth()
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
    // Table description
    Text(
        text = "If the net total is POSITIVE, you ARE OWED money.\n" +
                "If the net total is NEGATIVE, you OWE money.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(horizontal = medPadding)
    )
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