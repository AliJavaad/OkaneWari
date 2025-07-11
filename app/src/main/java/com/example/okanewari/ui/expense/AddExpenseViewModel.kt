package com.example.okanewari.ui.expense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.MemberModel
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.ui.components.ExpenseDetails
import com.example.okanewari.ui.components.ExpenseUiState
import com.example.okanewari.ui.components.MemberDetails
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import com.example.okanewari.ui.components.canConvertStringToBigDecimal
import com.example.okanewari.ui.components.toExpenseModel
import com.example.okanewari.ui.components.toMemberDetails
import com.example.okanewari.ui.components.toPartyModel
import com.example.okanewari.ui.components.toPartyUiState
import com.example.okanewari.ui.components.validateNameInput
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddExpenseViewModel(
    savedStateHandle: SavedStateHandle,
    private val owRepository: OkaneWariRepository
): ViewModel() {

    private val partyId: Long = checkNotNull(savedStateHandle[AddExpenseDestination.partyIdArg])

    var addExpenseUiState by mutableStateOf(AddExpenseUiState(expenseUiState = ExpenseUiState(
        ExpenseDetails(partyKey = partyId))))
        private set

    // Get the initial party info when entering the screen
    init {
        viewModelScope.launch {
            addExpenseUiState.partyUiState = owRepository.getPartyStream(partyId)
                .filterNotNull()
                .first()
                .toPartyUiState(true)
            // Reactive flow state for member list
            owRepository.getAllMembersFromParty(partyId)
                .filterNotNull()
                .collect{ dbMembers ->
                    addExpenseUiState = addExpenseUiState.copy(
                        memberList = dbMembers,
                        // Since the group owner will always be returned as index [0]
                        payingMember = dbMembers[0].toMemberDetails()
                    )
                }
        }
    }

    /**
     * Updates the [addExpenseUiState] with the value provided in the argument.
     * This method also triggers a validation for input values.
     */
    fun updateExpenseUiState(partyDetails: PartyDetails, expenseDetails: ExpenseDetails) {
        addExpenseUiState =
            AddExpenseUiState(
                expenseUiState = ExpenseUiState(expenseDetails, validateExpense(expenseDetails)),
                partyUiState = PartyUiState(partyDetails, true),
                memberList = addExpenseUiState.memberList,
                payingMember = addExpenseUiState.payingMember,
                owingMembers = addExpenseUiState.owingMembers
            )
    }

    fun updateSplitUiState(payingMember: MemberDetails, owingMembers: List<MemberModel>){
        addExpenseUiState =
            AddExpenseUiState(
                expenseUiState = addExpenseUiState.expenseUiState,
                partyUiState = addExpenseUiState.partyUiState,
                memberList = addExpenseUiState.memberList,
                payingMember = payingMember,
                owingMembers = owingMembers
            )
    }

    suspend fun saveExpense() {
        if (validateExpense()) {
            owRepository.insertExpense(addExpenseUiState.expenseUiState.expenseDetails.toExpenseModel())
        }
    }

    suspend fun updateParty(){
        owRepository.updateParty(addExpenseUiState.partyUiState.partyDetails.toPartyModel())
    }

    private fun validateExpense(uiState: ExpenseDetails = addExpenseUiState.expenseUiState.expenseDetails): Boolean {
        return with(uiState) {
            validateNameInput(name) && canConvertStringToBigDecimal(amount)
        }
    }
}

data class AddExpenseUiState(
    var partyUiState: PartyUiState = PartyUiState(PartyDetails()),
    val expenseUiState: ExpenseUiState = ExpenseUiState(ExpenseDetails()),
    val memberList: List<MemberModel> = listOf(),
    var payingMember: MemberDetails = MemberDetails(),
    var owingMembers: List<MemberModel> = listOf()
)


