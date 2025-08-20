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
import com.example.okanewari.ui.components.toExpenseDetails
import com.example.okanewari.ui.components.toMemberDetails
import com.example.okanewari.ui.components.toPartyDetails
import com.example.okanewari.ui.expense.EditExpenseDestination
import com.example.okanewari.ui.expense.EditExpenseViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.Date

class EditExpenseTesting {
    private lateinit var owDatabase: OkaneWariDatabase
    private lateinit var owRepository: OkaneWariRepository

    private val party1 = PartyModel(1, "test", "$", Date().time)
    private val member1 = MemberModel(1, 1, "javs")
    private val member2 = MemberModel(2, 1, "hal")
    private val member3 = MemberModel(3, 1, "ivan")
    private val member4 = MemberModel(4, 1, "kent")

    private val expense1 = ExpenseModel(1, 1, "food","300", Date().time)
    private val expense1_1 = ExpenseModel(1, 1, "drinks","400", Date().time)

    // Holders to check the split data
    private val splitList1 = listOf(
        SplitModel(1, 1,1,1, SplitType.PAID_AND_SPLIT, "200.00"),
        SplitModel(2, 1,1,2, SplitType.OWE, "-100.00"),
        SplitModel(3, 1,1,3, SplitType.OWE, "-100.00")
    )

    private val splitList1_1 = listOf(
        SplitModel(4, 1,1,2, SplitType.PAID_AND_SPLIT, "300.00"),
        SplitModel(5, 1,1,1, SplitType.OWE, "-100.00"),
        SplitModel(6, 1,1,3, SplitType.OWE, "-100.00"),
        SplitModel(7, 1,1,4, SplitType.OWE, "-100.00")
    )
    private val splitListPif = listOf(
        SplitModel(1, 1,1,1, SplitType.PAID_IN_FULL, "300"),
        SplitModel(2, 1,1,2, SplitType.OWE, "-150.00"),
        SplitModel(3, 1,1,3, SplitType.OWE, "-150.00")
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
    fun editBasicExpenseWithPaidAndSplit_CheckExpenseAndSplits() = runBlocking{
        // Prepopulate the database
        prePopulateDataWithPaidAndSplit()
        // init the viewmodel
        val testPartyId = 1L
        val expenseIdArg = 1L
        val savedStateHandle = SavedStateHandle().apply {
            set(EditExpenseDestination.partyIdArg, testPartyId)
            set(EditExpenseDestination.expenseIdArg, expenseIdArg)
        }
        val viewModel = EditExpenseViewModel(savedStateHandle, owRepository)

        // add test data to the viewmodel
        viewModel.updateExpenseUiState(
            expenseDetails = expense1_1.toExpenseDetails(),
            partyDetails = party1.toPartyDetails()
        )
        viewModel.updateSplitUiState(
            payingMember = member2.toMemberDetails(),
            owingMembers = listOf(member1, member3, member4),
            payType = SplitType.PAID_AND_SPLIT
        )

        // run the backend to add expense
        viewModel.updateExpenseAndSplit()

        // retrieve data from database and confirm the splits
        val expense = owRepository.getAllExpensesStream(1).first()
        assertEquals(expense1_1, expense[0])

        // Check the splits
        val splits = owRepository.getAllSplitsForExpense(1).first()
        assertEquals(splitList1_1, splits)
    }


    private suspend fun prePopulateDataWithPaidAndSplit(){
        owRepository.insertParty(party1)
        owRepository.insertMember(member1)
        owRepository.insertMember(member2)
        owRepository.insertMember(member3)
        owRepository.insertMember(member4)

        owRepository.insertExpense(expense1)

        owRepository.insertSplit(splitList1[0])
        owRepository.insertSplit(splitList1[1])
        owRepository.insertSplit(splitList1[2])
    }
}