package ir.miare.androidcodechallenge.usecase

import androidx.paging.PagingData
import androidx.paging.map
import ir.miare.androidcodechallenge.model.Player
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class GetPlayersPagerUseCaseTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var repository: FakePlayerRepository
    private lateinit var getPlayersPagerUseCase: GetPlayersPagerUseCase

    @Before
    fun setUp() {
        repository = FakePlayerRepository()
        getPlayersPagerUseCase = GetPlayersPagerUseCase(repository)
    }

    @Test
    fun `invoke returns paging data from repository`() = runTest {

        val player = Player("1", "Player 1", teamName = "", teamRank = 0, totalGoal = 0, leagueName = "", leagueCountry = "")
        val pagingData = PagingData.from(listOf(player))
        repository.playersFlow = flowOf(pagingData)


        val resultFlow = getPlayersPagerUseCase("name", true)


        val emitted = mutableListOf<PagingData<Player>>()
        resultFlow.collect { emitted.add(it) }

        assertEquals(1, emitted.size)
    }

    @Test
    fun `invoke returns ASC sort paging data from repository`() = runTest {

        val player1 = Player("1", "Player 1", teamName = "", teamRank = 0, totalGoal = 10, leagueName = "", leagueCountry = "")
        val player2 = Player("2", "Player 2", teamName = "", teamRank = 0, totalGoal = 11, leagueName = "", leagueCountry = "")
        val player3 = Player("3", "Player 3", teamName = "", teamRank = 0, totalGoal = 12, leagueName = "", leagueCountry = "")


        val pagingData = PagingData.from(listOf(player3, player1, player2))
        repository.playersFlow = flowOf(pagingData)


        val resultFlow = getPlayersPagerUseCase("goals", true)


        val emitted = mutableListOf<PagingData<Player>>()
        resultFlow.collect { emitted.add(it) }


        val collectedItems = mutableListOf<Player>()
        emitted.forEach { pd ->
            runBlocking {
                pd.map { player ->
                    collectedItems.add(player)
                }
            }
        }


        val isSortedAsc = collectedItems.zipWithNext { a, b -> a.totalGoal <= b.totalGoal }.all { it }
        assertTrue("The players should be sorted in ascending order", isSortedAsc)
    }



}