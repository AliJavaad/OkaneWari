import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.okanewari.data.OfflineOkaneWariRepository
import com.example.okanewari.data.OkaneWariDatabase
import com.example.okanewari.data.OkaneWariRepository
import com.example.okanewari.ui.expense.ListExpensesDestination
import com.example.okanewari.ui.expense.ListExpensesScreen
import com.example.okanewari.ui.expense.ListExpensesViewModel
import com.example.okanewari.ui.party.AddPartyScreen
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.jvm.Throws

class BasicUnitTest {
    private lateinit var owDatabase: OkaneWariDatabase
    private lateinit var owRepository: OkaneWariRepository

    @get:Rule
    val composeTestRule = createComposeRule()

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

    // TODO: Add test
    @Test
    fun addPartyScreenTest(){
        composeTestRule.setContent {
            AddPartyScreen(
                navigateBack = {},
                navigateUp = {}
            )
        }
        composeTestRule.onRoot().printToLog("currentLabelExists")
        composeTestRule
            .onNodeWithText("Add a new Party")
            .assertExists()
    }

    @Test
    fun listExpensesScreen(){
        val testPartyId = 1L
        val savedStateHandle = SavedStateHandle().apply {
            set(ListExpensesDestination.partyIdArg, testPartyId)
        }
        val viewModel = ListExpensesViewModel(savedStateHandle, owRepository)

        composeTestRule.setContent {
            ListExpensesScreen(
                onAddExpenseButtonClicked = {},
                onExpenseCardClick = {},
                onSettingsButtonClicked = {},
                onStatCardClicked = {},
                navigateUp = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onRoot().printToLog("listExpensesScreen")
        composeTestRule
            .onNodeWithText("Settings")
            .assertExists()
    }
}