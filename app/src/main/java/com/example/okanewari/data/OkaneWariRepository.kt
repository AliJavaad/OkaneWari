package com.example.okanewari.data

import kotlinx.coroutines.flow.Flow

interface OkaneWariRepository {
    /**
     * Retrieve all the parties from the the given data source.
     */
    fun getAllPartiesStream(): Flow<List<PartyModel>>

    /**
     * Retrieve a party from the given data source that matches with the [id].
     */
    fun getPartyStream(id: Long): Flow<PartyModel?>

    /**
     * Insert party in the data source
     */
    suspend fun insertParty(party: PartyModel): Long

    /**
     * Delete party from the data source
     */
    suspend fun deleteParty(party: PartyModel)

    /**
     * Update party in the data source
     */
    suspend fun updateParty(party: PartyModel)


    /**
     * Retrieve all the entries from the the given data source that match with the [partyId].
     */
    fun getAllExpensesStream(partyId: Long): Flow<List<ExpenseModel>>

    /**
     * Retrieve an entry from the given data source that matches with the [id] and [partyId].
     */
    fun getExpense(id: Long, partyId: Long): Flow<ExpenseModel?>

    /**
     * Insert expense in the data source
     */
    suspend fun insertExpense(expense: ExpenseModel)

    /**
     * Delete expense from the data source
     */
    suspend fun deleteExpense(expense: ExpenseModel)

    /**
     * Update expense in the data source
     */
    suspend fun updateExpense(expense: ExpenseModel)


    /**
     * Retrieve all the members from the the given data source that match with the [partyId].
     */
    fun getAllMembersFromParty(partyId: Long): Flow<List<MemberModel>>

    /**
     * Retrieve a member from the given data source that matches with the [id] and [partyId].
     */
    fun getMember(id: Long, partyId: Long): Flow<MemberModel?>

    /**
     * Insert member in the data source
     */
    suspend fun insertMember(member: MemberModel)

    /**
     * Delete member from the data source
     */
    suspend fun deleteMember(member: MemberModel)

    /**
     * Update member in the data source
     */
    suspend fun updateMember(member: MemberModel)

}