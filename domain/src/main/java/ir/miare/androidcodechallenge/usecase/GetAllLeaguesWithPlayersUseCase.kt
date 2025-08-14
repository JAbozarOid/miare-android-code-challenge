package ir.miare.androidcodechallenge.usecase

import ir.miare.androidcodechallenge.model.LeagueWithPlayers
import ir.miare.androidcodechallenge.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow

class GetAllLeaguesWithPlayersUseCase(
    private val repository: PlayerRepository
) {
    operator fun invoke(): Flow<List<LeagueWithPlayers>> {
        return repository.getAllLeaguesWithPlayers()
    }
}