package ir.miare.androidcodechallenge.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(players: List<PlayerEntity>)

    @Query("SELECT COUNT(*) FROM players")
    suspend fun count(): Int

    @RawQuery(observedEntities = [PlayerEntity::class, FollowedPlayerEntity::class])
    fun playersPagingSource(query: SupportSQLiteQuery): PagingSource<Int, PlayerEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM followed_players WHERE playerId = :id)")
    fun isFollowedFlow(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun follow(entity: FollowedPlayerEntity)

    @Query("DELETE FROM followed_players WHERE playerId = :id")
    suspend fun unfollow(id: String)

    @Query("SELECT p.* FROM players p INNER JOIN followed_players f ON p.id = f.playerId")
    fun getFollowedPlayers(): Flow<List<PlayerEntity>>
}