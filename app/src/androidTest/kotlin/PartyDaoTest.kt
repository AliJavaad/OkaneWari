import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.javs.okanewari.data.OkaneWariDatabase
import com.javs.okanewari.data.PartyDao
import com.javs.okanewari.data.PartyModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.Date
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class PartyDaoTest {
    private lateinit var partyDao: PartyDao
    private lateinit var owDatabase: OkaneWariDatabase

    private val party1 = PartyModel(1, "javs", "$", Date().time)
    private val party2 = PartyModel(2, "chad", "¥", Date().time)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        owDatabase = Room.inMemoryDatabaseBuilder(context, OkaneWariDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        partyDao = owDatabase.partyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        owDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun partyDaoInsert_insertOnePartyIntoDb() = runBlocking {
        addOnePartyToDb()
        val allPartys = partyDao.getAllRecords().first()
        assertEquals(allPartys[0], party1)
    }

    @Test
    @Throws(Exception::class)
    fun partyDaoInsert_insertTwoPartysIntoDb() = runBlocking {
        addTwoPartysToDb()
        val allPartys = partyDao.getAllRecords().first()
        assertEquals(allPartys[0], party1)
        assertEquals(allPartys[1], party2)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateParty_updatesPartyInDb() = runBlocking{
        addTwoPartysToDb()

        val testParty1 = PartyModel(1, "svaj", "£", Date().time)
        val testParty2 = PartyModel(2, "blue", "$", Date().time)
        partyDao.update(testParty1)
        partyDao.update(testParty2)

        val allPartys = partyDao.getAllRecords().first()
        assertEquals(allPartys[0], testParty1)
        assertEquals(allPartys[1], testParty2)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteParty_deletesAllPartiesFromDb() = runBlocking {
        addTwoPartysToDb()
        partyDao.delete(party1)
        partyDao.delete(party2)
        val allItems = partyDao.getAllRecords().first()
        assertTrue(allItems.isEmpty())
    }

    private suspend fun addOnePartyToDb() {
        partyDao.insert(party1)
    }

    private suspend fun addTwoPartysToDb() {
        partyDao.insert(party1)
        partyDao.insert(party2)
    }

}


