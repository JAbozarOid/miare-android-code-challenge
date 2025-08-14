package ir.miare.androidcodechallenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import ir.miare.androidcodechallenge.model.Player
import ir.miare.androidcodechallenge.usecase.FollowPlayerUseCase
import ir.miare.androidcodechallenge.usecase.GetAllLeaguesWithPlayersUseCase
import ir.miare.androidcodechallenge.usecase.GetFollowedPlayersUseCase
import ir.miare.androidcodechallenge.usecase.GetPlayersPagerUseCase
import ir.miare.androidcodechallenge.util.PlayerFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PlayersViewModel (
    private val getPlayersPagerUseCase: GetPlayersPagerUseCase,
    private val followPlayerUseCase: FollowPlayerUseCase,
    private val getFollowedPlayersUseCase: GetFollowedPlayersUseCase,
    private val getAllLeaguesWithPlayers: GetAllLeaguesWithPlayersUseCase
) : ViewModel() {

    private val _followedIds = MutableStateFlow<Set<String>>(emptySet())
    val followedIds: StateFlow<Set<String>> = _followedIds.asStateFlow()

    private val _selectedFilter = MutableStateFlow(PlayerFilter.NONE)
    val selectedFilter: StateFlow<PlayerFilter> = _selectedFilter

    init {
        observeFollowed()
    }

    fun setFilter(filter: PlayerFilter) {
        _selectedFilter.value = filter
    }

    val playersFlow: Flow<List<Any>> = combine(
        getAllLeaguesWithPlayers(),
        selectedFilter
    ) { leagues, filter ->
        when (filter) {
            PlayerFilter.NONE -> leagues
                .flatMap { it.players }
                .sortedByDescending { it.totalGoal }

            PlayerFilter.TOP_THREE_SCORES -> leagues.map { league ->
                league.copy(players = league.players.sortedByDescending { it.totalGoal }.take(3))
            }

            PlayerFilter.AVG_GOALS_PER_MATCH -> leagues
                .map { league ->
                    val avg = league.players.sumOf { it.totalGoal }.toDouble() / league.league.totalMatches
                    league.copy(avgGoalsPerMatch = avg)
                }
                .sortedByDescending { it.avgGoalsPerMatch }

            PlayerFilter.MOST_GOALS_BY_PLAYER -> leagues
                .flatMap { it.players }
                .sortedByDescending { it.totalGoal }
                .take(1)

            PlayerFilter.TEAM_AND_LEAGUE_RANKING -> leagues
                .sortedBy { it.league.rank }
                .map { league ->
                    league.copy(players = league.players.sortedBy { it.teamRank })
                }
        }
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