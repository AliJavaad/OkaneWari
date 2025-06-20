package com.example.okanewari.data

import kotlinx.coroutines.flow.Flow

class OfflineOkaneWariRepository(
    private val partyDao: PartyDao,
    private val expenseDao: ExpenseDao,
    private val memberDao: MemberDao
): OkaneWariRepository {
    /**
     * Party methods
     */
    override fun getAllPartiesStream(): Flow<List<PartyModel>> = partyDao.getAllRecords()

    override fun getPartyStream(id: Long): Flow<PartyModel?> = partyDao.getParty(id)

    override suspend fun insertParty(party: PartyModel): Long = partyDao.insert(party)


    override suspend fun deleteParty(party: PartyModel) = partyDao.delete(party)

    override suspend fun updateParty(party: PartyModel) = partyDao.update(party)

    /**
     * Expense methods
     */
    override fun getAllExpensesStream(partyId: Long): Flow<List<ExpenseModel>> =
        expenseDao.getAllExpensesForParty(partyId = partyId)

    override fun getExpense(id: Long, partyId: Long): Flow<ExpenseModel?> =
        expenseDao.getExpense(id = id, partyId = partyId)

    override suspend fun insertExpense(expense: ExpenseModel) = expenseDao.insert(expense)

    override suspend fun deleteExpense(expense: ExpenseModel) = expenseDao.delete(expense)

    override suspend fun updateExpense(expense: ExpenseModel) = expenseDao.update(expense)

    /**
     * Member methods
     */
    override fun getAllMembersFromParty(partyId: Long): Flow<List<MemberModel>> =
        memberDao.getAllMembersForParty(partyId = partyId)

    override fun getMember(id: Long, partyId: Long): Flow<MemberModel?> =
        memberDao.getMember(id = id, partyId = partyId)

    override suspend fun insertMember(member: MemberModel) = memberDao.insert(member)

    override suspend fun deleteMember(member: MemberModel) = memberDao.delete(member)

    override suspend fun updateMember(member: MemberModel) = memberDao.update(member)
}