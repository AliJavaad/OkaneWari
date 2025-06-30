package com.example.okanewari.ui.components

import com.example.okanewari.data.MemberModel

data class MemberUiState(
    val memberDetails: MemberDetails = MemberDetails(),
    val isEntryValid: Boolean = false
)

data class MemberDetails(
    val id: Long = 0,
    val partyKey: Long = 0,
    val name: String = "",
    val owner: Boolean = false
)

/**
 * Extension function to convert [MemberDetails] to [MemberModel].
 */
fun MemberDetails.toMemberModel(): MemberModel = MemberModel(
    id = id,
    partyKey = partyKey,
    name = name,
    owner = owner
)

/**
 * Extension function to convert [MemberModel] to [MemberUiState].
 */
fun MemberModel.toMemberUiState(isEntryValid: Boolean = false): MemberUiState = MemberUiState(
    memberDetails = this.toMemberDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [MemberModel] to [MemberDetails].
 */
fun MemberModel.toMemberDetails(): MemberDetails = MemberDetails(
    id = id,
    partyKey = partyKey,
    name = name,
    owner = owner
)


/**
 * Function to convert a list of [MemberModel] to a list of [MemberUiState].
 */
fun MemberModelListToDetails(modelList: List<MemberModel>): List<MemberDetails> {
    val toReturn: MutableList<MemberDetails> = mutableListOf()
    for (memberModel in modelList){
        toReturn += memberModel.toMemberDetails()
    }
    return toReturn
}