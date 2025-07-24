package com.example.okanewari.ui.party

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.data.SplitModel
import com.example.okanewari.data.SplitType
import com.example.okanewari.ui.components.MemberDetails
import com.example.okanewari.ui.components.MemberUiState
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import com.example.okanewari.ui.components.toExpenseDetails
import com.example.okanewari.ui.components.toMemberModel
import com.example.okanewari.ui.components.toMemberUiState
import com.example.okanewari.ui.components.toPartyModel
import com.example.okanewari.ui.components.toPartyUiState
import com.example.okanewari.ui.components.validateNameInput
import com.example.okanewari.ui.expense.calculateExpenseSplit
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal

/**
 * ViewModel to retrieve, update, or delete a member from the [OkaneWariRepository]'s data source.
 */
class EditMemberViewModel(
    savedStateHandle: SavedStateHandle,
    private val owRepository: OkaneWariRepository
) : ViewModel() {

    var editMemberUiState by mutableStateOf(EditMemberUiState())
        private set

    private val partyId: Long = checkNotNull(savedStateHandle[EditMemberDestination.partyIdArg])
    private val memberId: Long = checkNotNull(savedStateHandle[EditMemberDestination.memberIdArg])

    // Get the initial party and member info when entering the screen
    init {
        viewModelScope.launch {
            try{
                val partyModel = owRepository.getPartyStream(partyId)
                    .filterNotNull()
                    .first()
                val memberModel = owRepository.getMember(memberId, partyId)
                    .filterNotNull()
                    .first()

                editMemberUiState = editMemberUiState.copy(
                    partyUiState = partyModel.toPartyUiState(true),
                    memberUiState = memberModel.toMemberUiState(true),
                    topBarPartyName = memberModel.name
                )
            }catch (e: Exception){
                Log.e("EditMemberVM", "Failed to initialize data.", e)
            }

        }
    }

    fun updateUiState(partyDetails: PartyDetails, memberDetails: MemberDetails) {
        editMemberUiState =
            EditMemberUiState(
                memberUiState = MemberUiState(memberDetails, validateMember(memberDetails)),
                partyUiState = PartyUiState(partyDetails, true),
                topBarPartyName = editMemberUiState.topBarPartyName
            )
    }

    suspend fun updateMember() {
        if (validateMember()) {
            try{
                owRepository.updateMember(editMemberUiState.memberUiState.memberDetails.toMemberModel())
            }catch (e: Exception){
                Log.e("EditMemberVM", "Failed to update member.", e)
            }
        }
    }

    suspend fun deleteMember(){
        try{
            // If there are only 2 members left, cannot perform a proper transfer of debts so
            // delete all remaining expenses and then delete the member.
            val memList = owRepository.getAllMembersFromParty(partyId).filterNotNull().first()
            if(memList.size < 3){
                owRepository.deleteAllExpensesInParty(partyId)
                owRepository.deleteMember(editMemberUiState.memberUiState.memberDetails.toMemberModel())
                return
            }

            // Get split (ie. expense) for specific member
            val splitList = owRepository
                .getAllSplitsForMember(editMemberUiState.memberUiState.memberDetails.id)
                .filterNotNull()
                .first()

            // Check if there are any expenses involving the member. If not, it is safe to delete.
            if(splitList.isEmpty()){
                owRepository.deleteMember(editMemberUiState.memberUiState.memberDetails.toMemberModel())
                return
            }

            // For each expense, go in and adjust the split values
            splitList.forEach{ split ->
                // Get the expense
                val expense = owRepository.getExpense(split.expenseKey, partyId)
                    .filterNotNull()
                    .first()
                // Delete the split
                owRepository.deleteSplit(split)

                if (split.splitType == SplitType.PAID_AND_SPLIT || split.splitType == SplitType.PAID_IN_FULL){
                    // if the member was the person who paid, delete the expense entirely
                    owRepository.deleteExpense(expense)
                }else{
                    // else, must get a new split with the remaining members and update the split table
                    val expSplits = owRepository.getAllSplitsForExpense(expense.id).filterNotNull().first()
                    // The split retrieval is always ordered so that the creditor is the first entry
                    val payType = expSplits[0].splitType
                    assert(payType == SplitType.PAID_AND_SPLIT || payType == SplitType.PAID_IN_FULL)
                    // Calculate the splits based on the payType
                    val total = expense.amount.toBigDecimal()
                    var creditSplit = total
                    val debtSplit: BigDecimal?
                    if(payType == SplitType.PAID_IN_FULL){
                        debtSplit = calculateExpenseSplit(total = total, splitBy = BigDecimal(expSplits.size - 1))
                    }else{
                        debtSplit = calculateExpenseSplit(total = total, splitBy = BigDecimal(expSplits.size))
                        creditSplit = total.subtract(debtSplit)
                    }
                    expSplits.forEach{ newSplit ->
                        when (newSplit.splitType){
                            SplitType.PAID_IN_FULL, SplitType.PAID_AND_SPLIT-> {
                                owRepository.updateSplit(
                                    SplitModel(
                                        id = newSplit.id,
                                        partyKey = newSplit.partyKey,
                                        expenseKey = newSplit.expenseKey,
                                        memberKey = newSplit.memberKey,
                                        splitType = newSplit.splitType,
                                        splitAmount = creditSplit.toString()
                                    )
                                )
                            }
                            SplitType.OWE -> {
                                owRepository.updateSplit(
                                    SplitModel(
                                        id = newSplit.id,
                                        partyKey = newSplit.partyKey,
                                        expenseKey = newSplit.expenseKey,
                                        memberKey = newSplit.memberKey,
                                        splitType = newSplit.splitType,
                                        splitAmount = debtSplit.negate().toString()
                                    )
                                )
                            }
                        }
                    }
                }
            }
            owRepository.deleteMember(editMemberUiState.memberUiState.memberDetails.toMemberModel())
        }catch (e: Exception){
            Log.e("EditMemberVM", "Failed to delete member.", e)
        }
    }

    suspend fun updateParty(){
        try{
            owRepository.updateParty(editMemberUiState.partyUiState.partyDetails.toPartyModel())
        }catch (e: Exception){
            Log.e("EditMemberVM", "Failed to update party.", e)
        }

    }

    private fun validateMember(uiState: MemberDetails = editMemberUiState.memberUiState.memberDetails): Boolean {
        return with(uiState) {
            validateNameInput(name)
        }
    }
}

/**
 * Represents Ui State for editing a Party.
 */
data class EditMemberUiState(
    var memberUiState: MemberUiState = MemberUiState(MemberDetails()),
    var partyUiState: PartyUiState = PartyUiState(PartyDetails()),
    var topBarPartyName: String = ""
)