package com.example.okanewari.ui.components

import com.example.okanewari.data.ExpenseModel
import java.util.Date

/**
 * Represents Ui State for an expense.
 */
data class ExpenseUiState(
    val expenseDetails: ExpenseDetails = ExpenseDetails(),
    val isEntryValid: Boolean = false
)

data class ExpenseDetails(
    val id: Int = 0,
    val partyKey: Int = 0,
    val name: String = "",
    val amount: String = "0.00",
    val dateModded: Date = Date()
)

/**
 * Extension function to convert [ExpenseDetails] to [ExpenseModel].
 * If the value of [ExpenseDetails.numberOfMembers] is not a valid [Int],
 * then the numberOfMembers will be set to 1
 */
fun ExpenseDetails.toExpenseModel(): ExpenseModel = ExpenseModel(
    id = id,
    partyKey = partyKey,
    name = name,
    amount = amount,
    dateModded = dateModded.time
)

/**
 * Extension function to convert [ExpenseModel] to [ExpenseDetails]
 */
fun ExpenseModel.toExpenseUiState(): ExpenseUiState = ExpenseUiState(
    expenseDetails = this.toExpenseDetails()
)

/**
 * Extension function to convert [ExpenseModel] to [ExpenseDetails]
 */
fun ExpenseModel.toExpenseDetails(): ExpenseDetails = ExpenseDetails(
    id = id,
    partyKey = partyKey,
    name = name,
    amount = amount,
    dateModded = Date(dateModded)
)

fun canConvertStringToBigDecimal(value: String): Boolean{
    return value.matches(Regex("^\\d{1,8}(\\.\\d{1,2})?\$"))
}