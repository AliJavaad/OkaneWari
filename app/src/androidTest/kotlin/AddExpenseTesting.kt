import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.okanewari.data.ExpenseModel
import com.example.okanewari.data.MemberModel
import com.example.okanewari.data.OfflineOkaneWariRepository
import com.example.okanewari.data.OkaneWariDatabase
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.data.PartyModel
import com.example.okanewari.data.SplitType
import com.example.okanewari.ui.components.toExpenseDetails
import com.example.okanewari.ui.components.toExpenseUiState
import com.example.okanewari.ui.components.toMemberDetails
import com.example.okanewari.ui.components.toPartyDetails
import com.example.okanewari.ui.components.toPartyUiState
import com.example.okanewari.ui.expense.AddExpenseUiState
import com.example.okanewari.ui.expense.AddExpenseViewModel
import com.example.okanewari.ui.expense.ListExpensesDestination
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import java.util.Date
import kotlin.jvm.Throws

class AddExpenseTesting {
    private lateinit var owDatabase: OkaneWariDatabase
    private lateinit var owRepository: OkaneWariRepository

    private val party1 = PartyModel(1, "test", "$", Date().time)
    private val member1 = MemberModel(1, 1, "javs")
    private val member2 = MemberModel(2, 1, "hal")
    private val member3 = MemberModel(3, 1, "ivan")
    private val member4 = MemberModel(4, 1, "kent")
    private val expense1 = ExpenseModel(1, 1, "food","400", Date().time)

//    @get:Rule
//    val composeTestRule = createComposeRule()

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
    fun addExpenseAndCheckSplit() = runBlocking{
        // Prepopulate the database
        populatePartyAndMemberTables()
        // init the viewmodel
        val testPartyId = 1L
        val savedStateHandle = SavedStateHandle().apply {
            set(ListExpensesDestination.partyIdArg, testPartyId)
        }
        val viewModel = AddExpenseViewModel(savedStateHandle, owRepository)

        // add test data to the viewmodel
        viewModel.updateExpenseUiState(
            expenseDetails = expense1.toExpenseDetails(),
            partyDetails = party1.toPartyDetails()
        )
        viewModel.updateSplitUiState(
            payingMember = member1.toMemberDetails(),
            owingMembers = listOf(member2, member3),
            payType = SplitType.PAID_AND_SPLIT
        )

        // run the backend to add expense
        viewModel.saveExpenseAndSplit()

        // retrieve data from database and confirm the splits
        val expense = owRepository.getAllExpensesStream(1).first()
        assertEquals(expense1, expense[0])
    }

    private suspend fun populatePartyAndMemberTables(){
        owRepository.insertParty(party1)
        owRepository.insertMember(member1)
        owRepository.insertMember(member2)
        owRepository.insertMember(member3)
        owRepository.insertMember(member4)
    }
}