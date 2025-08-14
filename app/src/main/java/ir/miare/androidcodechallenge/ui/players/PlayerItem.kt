package ir.miare.androidcodechallenge.ui.players


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.miare.androidcodechallenge.model.Player

@Composable
fun PlayerItem(
    player: Player,
    isFollowed: Boolean,
    onToggleFollow: () -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Column(modifier = Modifier.weight(1f)) {
                Text(text = player.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "${player.teamName} • ${player.leagueName}", style = MaterialTheme.typography.bodySmall)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${player.totalGoal} goals", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onToggleFollow,
                    colors = if (isFollowed) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    else ButtonDefaults.outlinedButtonColors()
                ) {
                    Text(if (isFollowed) "Following" else "Follow")
                }
            }

           /* Column {
                Text(player.name, style = MaterialTheme.typography.titleMedium)
                Text("${player.teamName} - Goals: ${player.totalGoal}", style = MaterialTheme.typography.bodyMedium)
            }
            Text("Rank: ${player.teamRank}", style = MaterialTheme.typography.bodySmall)*/
        }
    }
}
