import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.miare.androidcodechallenge.usecase.FollowPlayerUseCase
import ir.miare.androidcodechallenge.usecase.GetAllLeaguesWithPlayersUseCase
import ir.miare.androidcodechallenge.usecase.GetFollowedPlayersUseCase
import ir.miare.androidcodechallenge.usecase.GetPlayersPagerUseCase
import ir.miare.androidcodechallenge.viewmodel.PlayersViewModel

class PlayersViewModelFactory(
    private val getPlayersPagerUseCase: GetPlayersPagerUseCase,
    private val followPlayerUseCase: FollowPlayerUseCase,
    private val getFollowedPlayersUseCase: GetFollowedPlayersUseCase,
    private val getAllLeaguesWithPlayers: GetAllLeaguesWithPlayersUseCase

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayersViewModel(
                getPlayersPagerUseCase,
                followPlayerUseCase,
                getFollowedPlayersUseCase,
                getAllLeaguesWithPlayers
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
