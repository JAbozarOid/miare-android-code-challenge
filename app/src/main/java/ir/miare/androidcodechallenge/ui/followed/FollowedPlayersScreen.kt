package ir.miare.androidcodechallenge.ui.followed


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ir.miare.androidcodechallenge.ui.players.PlayerItem
import ir.miare.androidcodechallenge.viewmodel.PlayersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowedPlayersScreen(
    viewModel: PlayersViewModel,
    onBack: () -> Unit
) {

    val followedFlow = viewModel.followedPlayersFlow()
    val followedPlayers by followedFlow.collectAsState(initial = emptyList())

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Followed Players", color = MaterialTheme.colorScheme.onPrimary) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }) { padding ->
        if (followedPlayers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You don't have any followed players",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(contentPadding = padding) {
                items(followedPlayers) { player ->
                    PlayerItem(
                        player = player,
                        isFollowed = true,
                        onToggleFollow = { viewModel.toggleFollow(player) }
                    )
                }
            }
        }
    }
}
