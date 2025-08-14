package ir.miare.androidcodechallenge.repository

import androidx.paging.PagingData
import ir.miare.androidcodechallenge.model.LeagueWithPlayers
import ir.miare.androidcodechallenge.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun getPlayersPaged(sortBy: String, ascending: Boolean): Flow<PagingData<Player>>

    suspend fun seedPlayersIfEmpty(json: String)
    suspend fun followPlayer(playerId: String)
    suspend fun unfollowPlayer(playerId: String)
    fun getFollowedPlayers(): Flow<List<Player>>

    fun getAllLeaguesWithPlayers() : Flow<List<LeagueWithPlayers>>
}