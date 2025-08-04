package com.example.okanewari.ui.components

import com.example.okanewari.data.PartyModel
import java.util.Date

/**
 * Represents Ui State for a party.
 */
data class PartyUiState(
    val partyDetails: PartyDetails = PartyDetails(),
    val isEntryValid: Boolean = false
)

data class PartyDetails(
    val id: Long = 0,
    val partyName: String = "",
    // Defualt currency symbol for a new party
    val currency: String = "¥",
    val dateModded: Date = Date()
)

/**
 * Extension function to convert [PartyDetails] to [PartyModel].
 * If the value of [PartyDetails.numberOfMembers] is not a valid [Int],
 * then the numberOfMembers will be set to 1
 */
fun PartyDetails.toPartyModel(): PartyModel = PartyModel(
    id = id,
    partyName = partyName,
    currency = currency,
    dateModded = dateModded.time
)

/**
 * Extension function to convert [PartyModel] to [AddPartyUiState]
 */
fun PartyModel.toPartyUiState(isEntryValid: Boolean = false): PartyUiState = PartyUiState(
    partyDetails = this.toPartyDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [PartyModel] to [PartyDetails]
 */
fun PartyModel.toPartyDetails(): PartyDetails = PartyDetails(
    id = id,
    partyName = partyName,
    currency = currency,
    dateModded = Date(dateModded)
)

fun validateNameInput(toCheck: String): Boolean{
    return toCheck.matches(Regex("^[[:alnum:] ]{1,60}"))
}