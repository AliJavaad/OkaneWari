package com.example.okanewari.ui.components

import com.example.okanewari.data.MemberModel

data class MemberUiState(
    val memberDetails: MemberDetails = MemberDetails(),
    val isEntryValid: Boolean = false
)

data class MemberDetails(
    val id: Int = 0,
    val partyKey: Int = 0,
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
fun MemberModel.toMemberUiState(): MemberUiState = MemberUiState(
    memberDetails = this.toMemberDetails()
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