package ir.miare.androidcodechallenge.ui.players

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ir.miare.androidcodechallenge.model.LeagueWithPlayers
import ir.miare.androidcodechallenge.model.Player
import ir.miare.androidcodechallenge.ui.filter.FilterRow
import ir.miare.androidcodechallenge.util.PlayerFilter
import ir.miare.androidcodechallenge.viewmodel.PlayersViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayersScreen(
    sortBy: String = "goals",
    ascending: Boolean = false,
    onOpenFollowed: () -> Unit = {},
    viewModel: PlayersViewModel
) {

    val pagerFlow = remember { viewModel.playersPager(sortBy, ascending) }
    val lazyPagingItems = pagerFlow.collectAsLazyPagingItems()
    val followedIds by viewModel.followedIds.collectAsState()

    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val playersOrLeagues by viewModel.playersFlow.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Players", color = MaterialTheme.colorScheme.onPrimary) },
                actions = {
                    IconButton(onClick = onOpenFollowed) {
                        Icon(
                            Icons.Default.Bookmark,
                            contentDescription = "Followed",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            FilterRow(
                selected = selectedFilter,
                onSelected = { viewModel.setFilter(it) }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (selectedFilter) {
                    PlayerFilter.NONE -> {
                        items(lazyPagingItems.itemCount) { index ->
                            val player = lazyPagingItems[index]
                            if (player != null) {
                                PlayerItem(
                                    player = player,
                                    isFollowed = followedIds.contains(player.id),
                                    onToggleFollow = { viewModel.toggleFollow(player) }
                                )
                            }
                        }

                        if (lazyPagingItems.loadState.append is LoadState.Loading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }


                        if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
                            item{
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                    }

                    PlayerFilter.MOST_GOALS_BY_PLAYER -> {
                        items(playersOrLeagues.filterIsInstance<Player>()) { player ->
                            PlayerItem(
                                player = player,
                                isFollowed = followedIds.contains(player.id),
                                onToggleFollow = { viewModel.toggleFollow(player) }
                            )
                        }
                    }

                    PlayerFilter.TOP_THREE_SCORES,
                    PlayerFilter.AVG_GOALS_PER_MATCH,
                    PlayerFilter.TEAM_AND_LEAGUE_RANKING -> {
                        items(playersOrLeagues.filterIsInstance<LeagueWithPlayers>()) { league ->
                            Text(
                                text = "${league.league.name} (${league.league.country})",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            league.players.forEach { player ->
                                PlayerItem(
                                    player = player,
                                    isFollowed = followedIds.contains(player.id),
                                    onToggleFollow = { viewModel.toggleFollow(player) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlayersList(
    lazyPagingItems: LazyPagingItems<Player>,
    followedIds: Set<String>,
    onToggleFollow: (Player) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(lazyPagingItems.itemCount) { index ->
                val player = lazyPagingItems[index]
                if (player != null) {
                    PlayerItem(
                        player = player,
                        isFollowed = followedIds.contains(player.id),
                        onToggleFollow = { onToggleFollow(player) }
                    )
                }
            }

            if (lazyPagingItems.loadState.append is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}


