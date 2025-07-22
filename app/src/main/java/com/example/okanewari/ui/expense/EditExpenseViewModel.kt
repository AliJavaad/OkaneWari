package com.example.okanewari.ui.expense

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.MemberModel
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.data.SplitModel
import com.example.okanewari.data.SplitType
import com.example.okanewari.ui.components.ExpenseDetails
import com.example.okanewari.ui.components.ExpenseUiState
import com.example.okanewari.ui.components.MemberDetails
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import com.example.okanewari.ui.components.canConvertStringToBigDecimal
import com.example.okanewari.ui.components.toExpenseModel
import com.example.okanewari.ui.components.toExpenseUiState
import com.example.okanewari.ui.components.toMemberDetails
import com.example.okanewari.ui.components.toPartyModel
import com.example.okanewari.ui.components.toPartyUiState
import com.example.okanewari.ui.components.validateNameInput
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal

class EditExpenseViewModel(
    savedStateHandle: SavedStateHandle,
    private val owRepository: OkaneWariRepository
): ViewModel() {
    private val partyId: Long = checkNotNull(savedStateHandle[EditExpenseDestination.partyIdArg])
    private val expenseId: Long = checkNotNull(savedStateHandle[EditExpenseDestination.expenseIdArg])

    var editExpenseUiState by mutableStateOf(EditExpenseUiState())
        private set

    init {
        viewModelScope.launch {
            // Load in the initial data (expense and party info)
            val expenseModel = owRepository.getExpense(expenseId, partyId)
                .filterNotNull()
                .first()
            val partyModel = owRepository.getPartyStream(partyId)
                .filterNotNull()
                .first()
            // The topBarExpenseName should only be updated at the initial screen creation stage.
            // Otherwise it will keep changing as the text field name is edited.
            editExpenseUiState = editExpenseUiState.copy(
                expenseUiState = expenseModel.toExpenseUiState(true),
                partyUiState = partyModel.toPartyUiState(true),
                topBarExpenseName = expenseModel.name
            )

            // Load and parse the Member and split info
            owRepository.getAllMembersFromParty(partyId)
                .filterNotNull()
                .combine(owRepository.getAllSplitsForExpense(expenseId).filterNotNull()){
                    allMems, allSplits ->
                    Pair(allMems, allSplits)
                }
                .collect{ (rawMembers, splitList) ->
                    // Process the members
                    val memberMap = rawMembers.associateBy { it.id }
                    editExpenseUiState = editExpenseUiState.copy(memberList = memberMap)

                    // Process Splits
                    var owingMembers: List<MemberModel> = listOf()
                    var payingMember: MemberDetails? = null

                    splitList.forEach{ split ->
                        if(split.splitType == SplitType.PAY){
                            payingMember = memberMap[split.memberKey]?.toMemberDetails()
                        }else{
                            owingMembers = owingMembers.plus(memberMap[split.memberKey]!!)
                        }
                    }

                    // Update the state
                    editExpenseUiState = editExpenseUiState.copy(
                        payingMember = payingMember ?: MemberDetails(),
                        owingMembers = owingMembers
                    )
                }
        }
    }

    fun updateExpenseUiState(partyDetails: PartyDetails, expenseDetails: ExpenseDetails) {
        editExpenseUiState =
            EditExpenseUiState(
                expenseUiState = ExpenseUiState(expenseDetails, validateInput(expenseDetails)),
                partyUiState = PartyUiState(partyDetails, true),
                memberList = editExpenseUiState.memberList,
                payingMember = editExpenseUiState.payingMember,
                owingMembers = editExpenseUiState.owingMembers,
                topBarExpenseName = editExpenseUiState.topBarExpenseName
            )
    }

    fun updateSplitUiState(payingMember: MemberDetails, owingMembers: List<MemberModel>){
        editExpenseUiState =
            EditExpenseUiState(
                expenseUiState = editExpenseUiState.expenseUiState,
                partyUiState = editExpenseUiState.partyUiState,
                memberList = editExpenseUiState.memberList,
                payingMember = payingMember,
                owingMembers = owingMembers,
                topBarExpenseName = editExpenseUiState.topBarExpenseName
            )
    }

    suspend fun updateExpenseAndSplit() {
        // Capture all states needed
        val currentState = editExpenseUiState
        val currentPayingMember = currentState.payingMember
        val currentOwingMembers = currentState.owingMembers

        if (validateInput() && currentOwingMembers.isNotEmpty()) {
            try{
                // First delete all the splits
                owRepository.deleteSplitByExpense(expenseId)
                // Update the expense
                owRepository.updateExpense(currentState.expenseUiState.expenseDetails.toExpenseModel())
                // Calculate the split value
                val total = BigDecimal(currentState.expenseUiState.expenseDetails.amount)
                val split = calculateExpenseSplit(total, BigDecimal(currentState.owingMembers.size + 1))
                // Now, save the split for the PAYER as (total - split).
                owRepository.insertSplit(
                    SplitModel(
                        partyKey = partyId,
                        expenseKey = expenseId,
                        memberKey = currentPayingMember.id,
                        splitType = SplitType.PAY,
                        splitAmount = total.subtract(split).toString()))
                // Next insert all splits for members that OWE as negative of the split
                for(member in currentOwingMembers){
                    Log.d("InsertOweLoop", "Owing mem: ${member.name}")
                    owRepository.insertSplit(
                        SplitModel(
                            partyKey = partyId,
                            expenseKey = expenseId,
                            memberKey = member.id,
                            splitType = SplitType.OWE,
                            splitAmount = split.negate().toString()))
                }
            }catch(e: Exception){
                Log.e("updateExpenseAndSplit", e.toString())
            }
        }
    }

    suspend fun updateParty(){
        owRepository.updateParty(editExpenseUiState.partyUiState.partyDetails.toPartyModel())
    }

    /**
     * Deletes the expense from the [OkaneWariRepository]'s data source.
     */
    suspend fun deleteExpense() {
        owRepository.deleteExpense(editExpenseUiState.expenseUiState.expenseDetails.toExpenseModel())
    }

    private fun validateInput(uiState: ExpenseDetails = editExpenseUiState.expenseUiState.expenseDetails): Boolean {
        return with(uiState) {
            validateNameInput(name) && canConvertStringToBigDecimal(amount)
        }
    }
}

/**
 * Represents Ui State for Editing an expense.
 */
data class EditExpenseUiState(
    var expenseUiState: ExpenseUiState = ExpenseUiState(ExpenseDetails()),
    var partyUiState: PartyUiState = PartyUiState(PartyDetails()),
    var topBarExpenseName: String = "",
    var memberList: Map<Long, MemberModel> = mapOf(),
    var payingMember: MemberDetails = MemberDetails(),
    var owingMembers: List<MemberModel> = listOf()
)