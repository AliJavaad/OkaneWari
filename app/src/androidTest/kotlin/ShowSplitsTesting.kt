import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.javs.okanewari.data.ExpenseModel
import com.javs.okanewari.data.MemberModel
import com.javs.okanewari.data.OfflineOkaneWariRepository
import com.javs.okanewari.data.OkaneWariDatabase
import com.javs.okanewari.data.OkaneWariRepository
import com.javs.okanewari.data.PartyModel
import com.javs.okanewari.data.SplitModel
import com.javs.okanewari.data.SplitType
import com.javs.okanewari.ui.expense.ShowSplitsDestination
import com.javs.okanewari.ui.expense.ShowSplitsViewModel
import com.javs.okanewari.ui.expense.Transaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.math.BigDecimal
import java.util.Date

class ShowSplitsTesting {
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

    private val finalPayments = listOf(
        Transaction(4L, 1L, BigDecimal("80.00")),
        Transaction(3L, 2L, BigDecimal("20.00"))
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
    fun simpleSplitTotals() = runTest {
        prePopulateDatabaseFull()
        val testPartyId = 1L
        // Verify members exist
        val members = owRepository.getAllMembersFromParty(testPartyId).first()
        Log.d("TEST", "Mems loaded: ${members.size}")
        assertTrue("No members found", members.isNotEmpty())

        // Verify splits exist
        val splits = owRepository.getAllSplitsForParty(testPartyId).first()
        Log.d("TEST", "Splits loaded: ${splits.size}")
        assertTrue("No splits found", splits.isNotEmpty())

        val savedStateHandle = SavedStateHandle().apply {
            set(ShowSplitsDestination.partyIdArg, testPartyId)
        }
        val viewModel = ShowSplitsViewModel(savedStateHandle, owRepository)

        // Wait for viewModel to update (manual waiting)
        var attempts = 0
        val maxAttempts = 100
        while (viewModel.showSplitsUiState.memberSplitTotals.isEmpty() && attempts < maxAttempts) {
            delay(100) // Wait 100ms between checks
            Log.d("TEST", "Waiting for viewmodel... Current state ${viewModel.showSplitsUiState.memberSplitTotals}")
            attempts++
        }

        assertEquals(finalPayments, viewModel.showSplitsUiState.transactions)
    }

    private suspend fun prePopulateDatabaseFull(){
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