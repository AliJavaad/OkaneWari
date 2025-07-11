package com.example.okanewari.ui.expense

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.okanewari.OkaneWareTopAppBar
import com.example.okanewari.R
import com.example.okanewari.data.MemberModel
import com.example.okanewari.navigation.NavigationDestination
import com.example.okanewari.ui.OwViewModelProvider
import com.example.okanewari.ui.components.DoneAndCancelButtons
import com.example.okanewari.ui.components.ExpenseDetails
import com.example.okanewari.ui.components.ExpenseUiState
import com.example.okanewari.ui.components.MemberDetails
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import com.example.okanewari.ui.components.toMemberDetails
import kotlinx.coroutines.launch
import java.util.Date

object AddExpenseDestination: NavigationDestination{
    override val route = "add_new_expense"
    override val titleRes = R.string.add_new_expense
    const val partyIdArg = "partyIdArg"
    val routeWithArg = "$route/{$partyIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    navigateUp: () -> Unit,
    navigateBack: () -> Unit,
    canNavigateBackBool: Boolean = true,
    viewModel: AddExpenseViewModel = viewModel(factory = OwViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            OkaneWareTopAppBar(
                title = stringResource(AddExpenseDestination.titleRes),
                canNavigateBack = canNavigateBackBool,
                navigateUp = navigateUp
            )
        }
    ){ innerPadding ->
        ExpenseEntryBody(
            expenseUiState = viewModel.addExpenseUiState.expenseUiState,
            partyUiState = viewModel.addExpenseUiState.partyUiState,
            memberList = viewModel.addExpenseUiState.memberList,
            payingMember = viewModel.addExpenseUiState.payingMember,
            owingMembers = viewModel.addExpenseUiState.owingMembers,
            onExpenseValueChange = viewModel::updateExpenseUiState,
            onSplitValueChange = viewModel::updateSplitUiState,
            onDone = {
                coroutineScope.launch{
                    viewModel.saveExpense()
                    viewModel.updateParty()
                    navigateBack()
                }
            },
            onCancel = navigateBack,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun ExpenseEntryBody(
    expenseUiState: ExpenseUiState,
    partyUiState: PartyUiState,
    memberList: List<MemberModel>,
    payingMember: MemberDetails,
    owingMembers: List<MemberModel>,
    onExpenseValueChange: (PartyDetails, ExpenseDetails) -> Unit,
    onSplitValueChange: (MemberDetails, List<MemberModel>) -> Unit,
    onDone: () -> Unit,
    onCancel: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = Modifier
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        ExpenseInputForm(
            expenseDetails = expenseUiState.expenseDetails,
            partyDetails = partyUiState.partyDetails,
            onValueChange = onExpenseValueChange
        )
        HorizontalDivider(
            thickness = 4.dp,
            modifier = Modifier.padding(all = dimensionResource(R.dimen.medium_padding))
        )
        SplitExpenseForm(
            memberList = memberList,
            payingMember = payingMember,
            owingMembers = owingMembers,
            onValueChange = onSplitValueChange
        )
        HorizontalDivider(
            thickness = 4.dp,
            modifier = Modifier.padding(all = dimensionResource(R.dimen.medium_padding))
        )
        DoneAndCancelButtons(
            doneButtonClick = onDone,
            cancelButtonClick = onCancel,
            enableDone = expenseUiState.isEntryValid
        )
    }
}

@Composable
fun ExpenseInputForm(
    expenseDetails: ExpenseDetails,
    partyDetails: PartyDetails,
    onValueChange: (PartyDetails, ExpenseDetails) -> Unit,
) {
    /**
     * Handling the expense name input field
     */
    TextField(
        value = expenseDetails.name,
        onValueChange = {
            onValueChange(partyDetails.copy(dateModded = Date()), expenseDetails.copy(name = it, dateModded = Date())) },
        label = { Text(stringResource(R.string.expense_name)) },
        placeholder = { Text(stringResource(R.string.expense)) },
        supportingText = {Text(stringResource(R.string.name_format_warning))},
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(R.dimen.medium_padding))
    )
    /**
     * Handling the money input field
     */
    TextField(
        value = expenseDetails.amount,
        onValueChange = {
            onValueChange(partyDetails.copy(dateModded = Date()), expenseDetails.copy(amount = it, dateModded = Date()))
        },
        label = { Text(stringResource(R.string.amount)) },
        placeholder = { Text(stringResource(R.string.zero)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        supportingText = { Text(stringResource(R.string.currency_format_warning)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(R.dimen.medium_padding))
    )
}

@Composable
fun SplitExpenseForm(
    memberList: List<MemberModel>,
    payingMember: MemberDetails,
    owingMembers: List<MemberModel>,
    onValueChange: (MemberDetails, List<MemberModel>) -> Unit
){
    val medPadding = dimensionResource(R.dimen.medium_padding)
    /**
     * Handling dropdown selection for who paid
     */
    var selectPayerDropdownMenu by rememberSaveable { mutableStateOf(false) }
    Row (
        modifier = Modifier.padding(all = medPadding)
    ) {
        Text(stringResource(R.string.paid_by) + ": ")
        Box(
            modifier = Modifier
                .width(dimensionResource(R.dimen.member_name_spacing))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable { selectPayerDropdownMenu = true }
        ){
            Image(
                painter = painterResource(R.drawable.baseline_arrow_drop_down_24),
                contentDescription = "asdf",
                modifier = Modifier.align(Alignment.TopEnd)
            )
            Text(
                text = payingMember.name,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.small_padding))
            )
            DropdownMenu(
                expanded = selectPayerDropdownMenu,
                onDismissRequest = { selectPayerDropdownMenu = false }
            ) {
                for(member in memberList){
                    DropdownMenuItem(
                        text = { Text(member.name)},
                        onClick = {
                            onValueChange(member.toMemberDetails().copy(), owingMembers.filterNot { it.id == member.id })
                            selectPayerDropdownMenu = false
                        }
                    )
                }
            }
        }
    }
    /**
     * Handling checklist for members splitting the expense
     */
    Text(
        text = stringResource(R.string.split_with) + ": ",
        modifier = Modifier.padding(horizontal = medPadding)
    )
    for(member in memberList){
        if(member.id == payingMember.id){
            continue
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                //.height(56.dp)
                .padding(horizontal = dimensionResource(R.dimen.large_padding))
        ){
            Checkbox(
                checked = owingMembers.contains(member),
                onCheckedChange = {
                    // If the member already exists in the list, that means it is being unchecked,
                    // so we need to remove it
                    if (owingMembers.contains(member)){
                        onValueChange(
                            payingMember.copy(), owingMembers.filterNot { it.id == member.id })
                    }else{
                        // Means the member isnt in the list so we need to add it
                        onValueChange(payingMember.copy(), owingMembers.plus(member))
                    }
                }
            )
            Text(
                text = member.name,
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.small_padding))
                    .padding(vertical = medPadding)
            )
        }
    }
    HorizontalDivider(
        thickness = 4.dp,
        modifier = Modifier.padding(all = dimensionResource(R.dimen.medium_padding))
    )
    for(member in owingMembers){
        Text(text = member.name, modifier = Modifier.padding(all = medPadding))
    }
}