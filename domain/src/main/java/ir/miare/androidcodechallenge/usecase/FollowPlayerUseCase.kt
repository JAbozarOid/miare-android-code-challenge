package ir.miare.androidcodechallenge.usecase

import ir.miare.androidcodechallenge.repository.PlayerRepository
import javax.inject.Inject
import javax.inject.Singleton

class FollowPlayerUseCase(
    private val repo: PlayerRepository) {
    suspend fun follow(playerId : String) {
        repo.followPlayer(playerId)
    }

    suspend fun unfollow(playerId: String) {
        repo.unfollowPlayer(playerId)
    }
}
