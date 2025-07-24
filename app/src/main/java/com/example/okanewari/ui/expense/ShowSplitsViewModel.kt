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
import com.example.okanewari.ui.components.PartyDetails
import com.example.okanewari.ui.components.PartyUiState
import com.example.okanewari.ui.components.toPartyUiState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ShowSplitsViewModel(
    savedStateHandle: SavedStateHandle,
    private val owRepository: OkaneWariRepository
): ViewModel() {
    private val partyId: Long = checkNotNull(savedStateHandle[EditExpenseDestination.partyIdArg])

    var showSplitsUiState by mutableStateOf(ShowSplitsUiState())
        private set

    init {
        viewModelScope.launch {
            try{
                // Get party info
                val partyModel = owRepository.getPartyStream(partyId)
                    .filterNotNull()
                    .first()
                showSplitsUiState = showSplitsUiState.copy(
                    partyUiState = partyModel.toPartyUiState(true),
                    topBarExpenseName = partyModel.partyName
                )

                // Load and parse the Member and split info
                owRepository.getAllMembersFromParty(partyId)
                    .filterNotNull()
                    .combine(
                        owRepository.getAllSplitsForParty(partyId).filterNotNull()
                    ) { allMems, allSplits ->
                        Pair(allMems, allSplits)
                    }
                    .collect { (rawMembers, splitList) ->
                        // Process the members
                        val memberMap = rawMembers.associateBy { it.id }

                        // Process Splits
                        val memberSplitTotals =
                            memberMap.map { it.key to BigDecimal.ZERO }.toMap().toMutableMap()

                        splitList.forEach { split ->
                            memberSplitTotals[split.memberKey] =
                                BigDecimal(split.splitAmount).add(memberSplitTotals[split.memberKey])
                        }

                        val transactions = calculateTransactions(memberSplitTotals)

                        // Update the state
                        showSplitsUiState = showSplitsUiState.copy(
                            memberList = memberMap,
                            memberSplitTotals = memberSplitTotals,
                            transactions = transactions
                        )
                    }
            }catch (e: Exception){
                Log.e("ShowSplitVM", "Failed to initialize the data.", e)
            }
        }
    }

    suspend fun deleteAllExpenses(){
        try{
            owRepository.deleteAllExpensesInParty(partyId)
        }catch(e: Exception){
            Log.e("ShowSplitVM", "Failed to delete all expenses.", e)
        }
    }
}

/**
 * Represents Ui State for Showing the splits.
 */
data class ShowSplitsUiState(
    var partyUiState: PartyUiState = PartyUiState(PartyDetails()),
    var memberList: Map<Long, MemberModel> = mapOf(),
    var memberSplitTotals: Map<Long, BigDecimal> = mapOf(),
    var transactions: List<Transaction> = listOf(),
    var topBarExpenseName: String = "",
)

fun calculateTransactions(splitTotals: Map<Long, BigDecimal>): List<Transaction>{
    val transactions = mutableListOf<Transaction>()
    val creditors = mutableListOf<NetTotals>()
    val debtors = mutableListOf<NetTotals>()

    // Separate into people who are owed (creditors) and people who owe (debtors)
    splitTotals.forEach{
        when{
            it.value > BigDecimal("0.1") -> creditors.add(NetTotals(it.key, it.value))
            it.value < BigDecimal("-0.1") -> debtors.add(NetTotals(it.key, it.value))
        }
    }.also{
        creditors.sortByDescending { it.total }
        debtors.sortBy { it.total }
    }

    var c = 0
    var d = 0
    while ( c < creditors.size && d < debtors.size){
        val credit = creditors[c]
        val debt = debtors[d]
        val amount = minOf(credit.total, -debt.total)

        transactions.add(Transaction(debt.key, credit.key, amount))

        credit.total -= amount
        debt.total += amount

        if (credit.total < BigDecimal("0.1")) c++
        if (debt.total > BigDecimal("-0.1")) d++
    }

    return transactions
}

data class NetTotals(
    var key: Long,
    var total: BigDecimal
)

data class Transaction(
    val fromKey: Long,
    val toKey: Long,
    val amount: BigDecimal
)