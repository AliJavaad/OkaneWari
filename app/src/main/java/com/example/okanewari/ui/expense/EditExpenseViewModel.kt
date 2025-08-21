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
import kotlinx.coroutines.ensureActive
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
            try{
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
                        var payType = SplitType.PAID_AND_SPLIT

                        splitList.forEach{ split ->
                            if(split.splitType == SplitType.PAID_AND_SPLIT || split.splitType == SplitType.PAID_IN_FULL){
                                payingMember = memberMap[split.memberKey]?.toMemberDetails()
                                payType = split.splitType
                            }else{
                                owingMembers = owingMembers.plus(memberMap[split.memberKey]!!)
                            }
                        }

                        // Update the state
                        editExpenseUiState = editExpenseUiState.copy(
                            payingMember = payingMember ?: MemberDetails(),
                            owingMembers = owingMembers,
                            payType = payType
                        )
                    }
            } catch (e: Exception){
                coroutineContext.ensureActive()
                Log.e("EditExpenseVM", "Failed to initialize the data.", e)
            }
        }
    }

    fun updateExpenseUiState(partyDetails: PartyDetails, expenseDetails: ExpenseDetails) {
        editExpenseUiState = editExpenseUiState.copy(
            expenseUiState = ExpenseUiState(expenseDetails, validateInput(expenseDetails)),
            partyUiState = PartyUiState(partyDetails, true)
        )
    }

    fun updateSplitUiState(payingMember: MemberDetails, owingMembers: List<MemberModel>, payType: SplitType){
        editExpenseUiState = editExpenseUiState.copy(
            payingMember = payingMember,
            owingMembers = owingMembers,
            payType = payType
        )
    }

    suspend fun updateExpenseAndSplit() {
        // Capture all states needed
        val currentState = editExpenseUiState
        val currentPayingMember = currentState.payingMember
        val currentOwingMembers = currentState.owingMembers
        val currentPayType = currentState.payType

        if (validateInput() && currentOwingMembers.isNotEmpty()) {
            // First delete all the splits
            owRepository.deleteSplitByExpense(expenseId)
            // Update the expense
            owRepository.updateExpense(currentState.expenseUiState.expenseDetails.toExpenseModel())
            // Calculate the split value
            val total = BigDecimal(currentState.expenseUiState.expenseDetails.amount)
            var creditSplit = total
            val debtSplit: BigDecimal?
            if(currentPayType == SplitType.PAID_IN_FULL){
                debtSplit = calculateExpenseSplit(total = total, splitBy = BigDecimal(currentState.owingMembers.size))
            }else{
                debtSplit = calculateExpenseSplit(total = total, splitBy = BigDecimal(currentState.owingMembers.size + 1 ))
                creditSplit = total.subtract(debtSplit)
            }
            // Now, save the split for the PAYER as (total - split).
            owRepository.insertSplit(
                SplitModel(
                    partyKey = partyId,
                    expenseKey = expenseId,
                    memberKey = currentPayingMember.id,
                    splitType = currentPayType,
                    splitAmount = creditSplit.toString()))
            // Next insert all splits for members that OWE as negative of the split
            for(member in currentOwingMembers){
                owRepository.insertSplit(
                    SplitModel(
                        partyKey = partyId,
                        expenseKey = expenseId,
                        memberKey = member.id,
                        splitType = SplitType.OWE,
                        splitAmount = debtSplit.negate().toString()))
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
    var owingMembers: List<MemberModel> = listOf(),
    var payType: SplitType = SplitType.PAID_AND_SPLIT
)