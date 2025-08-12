package ir.miare.androidcodechallenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.miare.androidcodechallenge.model.Player
import ir.miare.androidcodechallenge.usecase.FollowPlayerUseCase
import ir.miare.androidcodechallenge.usecase.GetFollowedPlayersUseCase
import ir.miare.androidcodechallenge.usecase.GetPlayersPagerUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
    private val getPlayersPagerUseCase: GetPlayersPagerUseCase,
    private val followPlayerUseCase: FollowPlayerUseCase,
    private val getFollowedPlayersUseCase: GetFollowedPlayersUseCase
) : ViewModel() {

    private val _followedIds = MutableStateFlow<Set<String>>(emptySet())
    val followedIds: StateFlow<Set<String>> = _followedIds.asStateFlow()

    init {
        observeFollowed()
    }

    private fun observeFollowed() {
        viewModelScope.launch {
            getFollowedPlayersUseCase().collect { list ->
                _followedIds.value = list.map { it.id }.toSet()
            }
        }
    }

    fun playersPager(sortBy: String = "goals", ascending: Boolean = false): Flow<PagingData<Player>> {
        return getPlayersPagerUseCase(sortBy, ascending).cachedIn(viewModelScope)
    }


    fun toggleFollow(player: Player) {
        viewModelScope.launch {
            if (_followedIds.value.contains(player.id)) {
                followPlayerUseCase.unfollow(player.id)
            } else {
                followPlayerUseCase.follow(player.id)
            }
        }
    }


    fun followedPlayersFlow() = getFollowedPlayersUseCase()
}