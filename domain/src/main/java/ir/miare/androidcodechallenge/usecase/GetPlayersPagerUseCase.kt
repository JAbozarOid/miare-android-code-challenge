package ir.miare.androidcodechallenge.usecase


import androidx.paging.PagingData
import ir.miare.androidcodechallenge.model.Player
import ir.miare.androidcodechallenge.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class GetPlayersPagerUseCase (
    private val repo: PlayerRepository) {
    operator fun invoke(sortBy: String, ascending: Boolean): Flow<PagingData<Player>> {
        return repo.getPlayersPaged(sortBy, ascending)
    }
}
