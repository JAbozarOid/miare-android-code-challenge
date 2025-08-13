package ir.miare.androidcodechallenge.ui.players

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ir.miare.androidcodechallenge.model.Player
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


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Players") },
                actions = {
                    IconButton(onClick = onOpenFollowed) {
                        Icon(Icons.Default.Bookmark, contentDescription = "Followed")
                    }
                }
            )
        }
    ) { padding ->
        PlayersList(
            lazyPagingItems = lazyPagingItems,
            followedIds = followedIds,
            onToggleFollow = { player -> viewModel.toggleFollow(player) },
            modifier = Modifier.padding(padding)
        )
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

