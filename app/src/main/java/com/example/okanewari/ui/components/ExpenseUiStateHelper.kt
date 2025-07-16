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
    val id: Long = 0,
    val partyKey: Long = 0,
    val name: String = "",
    val amount: String = "0.00",
    val dateModded: Date = Date()
)

/**
 * Extension function to convert [ExpenseDetails] to [ExpenseModel].
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
fun ExpenseModel.toExpenseUiState(isEntryValid: Boolean = false): ExpenseUiState = ExpenseUiState(
    expenseDetails = this.toExpenseDetails(),
    isEntryValid = isEntryValid
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
    return value.matches(Regex("^(?!0+\\.?0*\$)\\d{1,8}(\\.\\d{1,2})?\$"))
}