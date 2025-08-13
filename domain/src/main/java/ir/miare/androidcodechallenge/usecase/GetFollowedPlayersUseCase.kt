package ir.miare.androidcodechallenge.usecase

import ir.miare.androidcodechallenge.model.Player
import ir.miare.androidcodechallenge.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class GetFollowedPlayersUseCase (private val repo: PlayerRepository) {
    operator fun invoke(): Flow<List<Player>> {
        return repo.getFollowedPlayers()
    }
}
