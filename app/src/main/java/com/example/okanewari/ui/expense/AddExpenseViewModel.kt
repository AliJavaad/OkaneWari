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
import com.example.okanewari.ui.components.toMemberDetails
import com.example.okanewari.ui.components.toPartyModel
import com.example.okanewari.ui.components.toPartyUiState
import com.example.okanewari.ui.components.validateNameInput
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

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
            try{
                val partyModel = owRepository.getPartyStream(partyId)
                    .filterNotNull()
                    .first()
                addExpenseUiState = addExpenseUiState.copy(
                    partyUiState = partyModel.toPartyUiState(true)
                )
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
            } catch(e: Exception){
                Log.e("AddExpenseVM", "Failed to initialize the data.", e)
            }
        }
    }

    /**
     * Updates the [addExpenseUiState] with the value provided in the argument.
     * This method also triggers a validation for input values.
     */
    fun updateExpenseUiState(partyDetails: PartyDetails, expenseDetails: ExpenseDetails) {
        addExpenseUiState = addExpenseUiState.copy(
            expenseUiState = ExpenseUiState(expenseDetails, validateExpense(expenseDetails)),
            partyUiState = PartyUiState(partyDetails, true)
        )
    }

    fun updateSplitUiState(payingMember: MemberDetails, owingMembers: List<MemberModel>, payType: SplitType){
        addExpenseUiState = addExpenseUiState.copy(
            payingMember = payingMember,
            owingMembers = owingMembers,
            payType = payType
        )
    }

    suspend fun saveExpenseAndSplit() {
        // Capture all states needed
        val currentState = addExpenseUiState
        val currentPayingMember = currentState.payingMember
        val currentOwingMembers = currentState.owingMembers
        val currentPayType = currentState.payType

        if (validateExpense() && currentOwingMembers.isNotEmpty()) {
            try{
                // First save the expense in general and get the id.
                val expKey = owRepository.insertExpense(currentState.expenseUiState.expenseDetails.toExpenseModel())
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
                Log.d("Split", "The total is $total the credit is $creditSplit and debt is $debtSplit")
                // Now, save the split for the PAYER as (total - split).
                owRepository.insertSplit(
                    SplitModel(
                        partyKey = partyId,
                        expenseKey = expKey,
                        memberKey = currentPayingMember.id,
                        splitType = currentPayType,
                        splitAmount = creditSplit.toString()))
                // Next insert all splits for members that OWE as negative of the split
                for(member in currentOwingMembers){
                    owRepository.insertSplit(
                        SplitModel(
                            partyKey = partyId,
                            expenseKey = expKey,
                            memberKey = member.id,
                            splitType = SplitType.OWE,
                            splitAmount = debtSplit.negate().toString()))
                }
            }catch(e: Exception){
                Log.e("AddExpenseVM", "Failed to save expense and splits.", e)
            }
        }
    }

    suspend fun updateParty(){
        try{
            owRepository.updateParty(addExpenseUiState.partyUiState.partyDetails.toPartyModel())
        }catch(e: Exception){
            Log.e("AddExpenseVM", "Failed to update the party.", e)
        }

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
    var owingMembers: List<MemberModel> = listOf(),
    var payType: SplitType = SplitType.PAID_AND_SPLIT
)

fun calculateExpenseSplit(total: BigDecimal, splitBy: BigDecimal): BigDecimal{
    var split = BigDecimal.ZERO
    if (total != BigDecimal.ZERO){
        split = total.divide(splitBy, 2, RoundingMode.UP)
    }
    return split
}
