package ir.miare.androidcodechallenge.usecase

import androidx.paging.PagingData
import ir.miare.androidcodechallenge.model.LeagueWithPlayers
import ir.miare.androidcodechallenge.model.Player
import ir.miare.androidcodechallenge.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakePlayerRepository : PlayerRepository {
    var playersFlow: Flow<PagingData<Player>> = flowOf(PagingData.from(emptyList()))

    override fun getPlayersPaged(sortBy: String, ascending: Boolean) = playersFlow
    override suspend fun seedPlayersIfEmpty(json: String) {}
    override suspend fun followPlayer(playerId: String) {}
    override suspend fun unfollowPlayer(playerId: String) {}
    override fun getFollowedPlayers(): Flow<List<Player>> = flowOf(emptyList())
    override fun getAllLeaguesWithPlayers(): Flow<List<LeagueWithPlayers>> = flowOf(emptyList())
}