import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.okanewari.data.ExpenseModel
import com.example.okanewari.data.MemberModel
import com.example.okanewari.data.OfflineOkaneWariRepository
import com.example.okanewari.data.OkaneWariDatabase
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.data.PartyModel
import com.example.okanewari.data.SplitModel
import com.example.okanewari.data.SplitType
import com.example.okanewari.ui.components.toMemberDetails
import com.example.okanewari.ui.components.toPartyDetails
import com.example.okanewari.ui.party.EditMemberDestination
import com.example.okanewari.ui.party.EditMemberViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.Date

class DeleteMemberTesting {
    private lateinit var owDatabase: OkaneWariDatabase
    private lateinit var owRepository: OkaneWariRepository

    private val party1 = PartyModel(1, "test", "$", Date().time)
    private val member1 = MemberModel(1, 1, "javs")
    private val member2 = MemberModel(2, 1, "hal")
    private val member3 = MemberModel(3, 1, "ivan")
    private val member4 = MemberModel(4, 1, "kent")

    private val expense1 = ExpenseModel(1, 1, "food","300", Date().time)
    private val split1_1 = SplitModel(1, 1, 1, 1, SplitType.PAID_AND_SPLIT, "200.00")
    private val split1_2 = SplitModel(2, 1, 1, 2, SplitType.OWE, "-100.00")
    private val split1_3 = SplitModel(3, 1, 1, 3, SplitType.OWE, "-100.00")

    private val expense2 = ExpenseModel(2, 1, "drinks","180", Date().time)
    private val split2_1 = SplitModel(4, 1, 2, 1, SplitType.OWE, "-60.00")
    private val split2_2 = SplitModel(5, 1, 2, 2, SplitType.PAID_IN_FULL, "180.00")
    private val split2_3 = SplitModel(6, 1, 2, 3, SplitType.OWE, "-60.00")
    private val split2_4 = SplitModel(7, 1, 2, 4, SplitType.OWE, "-60.00")

    private val expense3 = ExpenseModel(3, 1, "car","200", Date().time)
    private val split3_1 = SplitModel(8, 1, 3, 1, SplitType.OWE, "-50.00")
    private val split3_2 = SplitModel(9, 1, 3, 2, SplitType.OWE, "-50.00")
    private val split3_3 = SplitModel(10, 1, 3, 3, SplitType.PAID_AND_SPLIT, "150.00")
    private val split3_4 = SplitModel(11, 1, 3, 4, SplitType.OWE, "-50.00")

    private val expense4 = ExpenseModel(4, 1, "gas","30", Date().time)
    private val split4_1 = SplitModel(12, 1, 4, 1, SplitType.OWE, "-10.00")
    private val split4_2 = SplitModel(13, 1, 4, 2, SplitType.OWE, "-10.00")
    private val split4_3 = SplitModel(14, 1, 4, 3, SplitType.OWE, "-10.00")
    private val split4_4 = SplitModel(15, 1, 4, 4, SplitType.PAID_IN_FULL, "30.00")

    // Holders to check the split data
    private val splitListExpense1 = listOf(
        SplitModel(1, 1,1,1, SplitType.PAID_AND_SPLIT, "200.00"),
        SplitModel(2, 1,1,2, SplitType.OWE, "-100.00"),
        SplitModel(3, 1,1,3, SplitType.OWE, "-100.00")
    )
    private val splitListExpense2 = listOf(
        SplitModel(5, 1,2,2, SplitType.PAID_IN_FULL, "180"),
        SplitModel(4, 1,2,1, SplitType.OWE, "-90.00"),
        SplitModel(6, 1,2,3, SplitType.OWE, "-90.00")
    )
    private val splitListExpense3 = listOf(
        SplitModel(10, 1,3,3, SplitType.PAID_AND_SPLIT, "133.33"),
        SplitModel(8, 1,3,1, SplitType.OWE, "-66.67"),
        SplitModel(9, 1,3,2, SplitType.OWE, "-66.67")
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        owDatabase = Room.inMemoryDatabaseBuilder(context, OkaneWariDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        owRepository = OfflineOkaneWariRepository(
            partyDao = owDatabase.partyDao(),
            expenseDao = owDatabase.expenseDao(),
            memberDao = owDatabase.memberDao(),
            splitDao = owDatabase.splitDao()
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        owDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun deleteMemberFromParty_CheckExpensesSplits() = runBlocking{
        // Prepopulate the database
        prePopulateDatabase()
        // init the viewmodel
        val testPartyId = 1L
        val testMemberId = 4L
        val savedStateHandle = SavedStateHandle().apply {
            set(EditMemberDestination.partyIdArg, testPartyId)
            set(EditMemberDestination.memberIdArg, testMemberId)
        }
        val viewModel = EditMemberViewModel(savedStateHandle, owRepository)

        // Execute the delete member for member 4 "kent"
        viewModel.updateUiState(
            partyDetails = party1.toPartyDetails(),
            memberDetails = member4.toMemberDetails(),
        )
        viewModel.deleteMember()

        // Retrieve the splits, expense 4 should be deleted
        val splits1 = owRepository.getAllSplitsForExpense(1).first()
        assertEquals(splitListExpense1, splits1)

        val splits2 = owRepository.getAllSplitsForExpense(2).first()
        assertEquals(splitListExpense2, splits2)

        val splits3 = owRepository.getAllSplitsForExpense(3).first()
        assertEquals(splitListExpense3, splits3)

        val splits4 = owRepository.getAllSplitsForExpense(4).first()
        assertTrue(splits4.isEmpty())
        val exp4 = owRepository.getExpense(4, 1).first()
        assertNull(exp4)
    }

    private suspend fun prePopulateDatabase(){
        owRepository.insertParty(party1)
        owRepository.insertMember(member1)
        owRepository.insertMember(member2)
        owRepository.insertMember(member3)
        owRepository.insertMember(member4)

        owRepository.insertExpense(expense1)
        owRepository.insertSplit(split1_1)
        owRepository.insertSplit(split1_2)
        owRepository.insertSplit(split1_3)

        owRepository.insertExpense(expense2)
        owRepository.insertSplit(split2_1)
        owRepository.insertSplit(split2_2)
        owRepository.insertSplit(split2_3)
        owRepository.insertSplit(split2_4)

        owRepository.insertExpense(expense3)
        owRepository.insertSplit(split3_1)
        owRepository.insertSplit(split3_2)
        owRepository.insertSplit(split3_3)
        owRepository.insertSplit(split3_4)

        owRepository.insertExpense(expense4)
        owRepository.insertSplit(split4_1)
        owRepository.insertSplit(split4_2)
        owRepository.insertSplit(split4_3)
        owRepository.insertSplit(split4_4)
    }
}