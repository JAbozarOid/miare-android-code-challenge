package ir.miare.androidcodechallenge.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.sqlite.db.SimpleSQLiteQuery
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ir.miare.androidcodechallenge.local.AppDatabase
import ir.miare.androidcodechallenge.local.FollowedPlayerEntity
import ir.miare.androidcodechallenge.local.PlayerDao
import ir.miare.androidcodechallenge.local.PlayerEntity
import ir.miare.androidcodechallenge.mapper.toDomain
import ir.miare.androidcodechallenge.mapper.toPlayerEntity
import ir.miare.androidcodechallenge.model.Player
import ir.miare.androidcodechallenge.remote.FakeData
import ir.miare.androidcodechallenge.remote.PlayerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PlayerRepositoryImpl(
    private val api: PlayerApi,
    private val db: AppDatabase
) : PlayerRepository {

    private val dao: PlayerDao = db.playerDao()

    override fun getPlayersPaged(
        sortBy: String,
        ascending: Boolean
    ): Flow<PagingData<Player>> {
        val sql = buildPagingSql(sortBy, ascending)
        val query = SimpleSQLiteQuery(sql)
        val pager = Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { dao.playersPagingSource(query) }
        )
        return pager.flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }

    override suspend fun seedPlayersIfEmpty(json: String) {
        if (dao.count() > 0) return

        val objectMapper = ObjectMapper().findAndRegisterModules()
        val fakeList: List<FakeData> = objectMapper.readValue(json)

        val entities = mutableListOf<PlayerEntity>()
        for (leagueData in fakeList) {
            val leagueName = leagueData.league.name
            val leagueCountry = leagueData.league.country
            leagueData.players.forEach { playerDto ->
                entities.add(playerDto.toPlayerEntity(leagueName, leagueCountry))
            }
        }
        db.runInTransaction {
            runBlocking {
                dao.insertAll(entities)
            }
        }
    }

    override suspend fun followPlayer(playerId: String) {
        dao.follow(FollowedPlayerEntity(playerId))
    }

    override suspend fun unfollowPlayer(playerId: String) {
        dao.unfollow(playerId)
    }

    override fun getFollowedPlayers(): Flow<List<Player>> {
        return dao.getFollowedPlayers().map { list -> list.map { it.toDomain() } }
    }

    private fun buildPagingSql(sortBy: String, asc: Boolean): String {
        val col = when (sortBy) {
            "goals" -> "totalGoal"
            "name" -> "name"
            "teamRank" -> "teamRank"
            "league" -> "leagueName"
            else -> "totalGoal"
        }
        val order = if (asc) "ASC" else "DESC"
        return "SELECT * FROM players ORDER BY $col $order"
    }
}