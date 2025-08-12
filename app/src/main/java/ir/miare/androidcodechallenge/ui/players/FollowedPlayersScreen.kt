package ir.miare.androidcodechallenge.ui.players


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ir.miare.androidcodechallenge.viewmodel.PlayersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowedPlayersScreen(viewModel: PlayersViewModel = hiltViewModel()) {
    val followedFlow = viewModel.followedPlayersFlow()
    val followedPlayers by followedFlow.collectAsState(initial = emptyList())

    Scaffold(topBar = {
        TopAppBar(title = { Text("Followed Players") })
    }) { padding ->
        LazyColumn(contentPadding = padding) {
            items(followedPlayers) { player ->
                PlayerItem(
                    player = player,
                    isFollowed = true,
                    onToggleFollow = { viewModel.toggleFollow(player) } // toggle will unfollow
                )
            }
        }
    }
}
