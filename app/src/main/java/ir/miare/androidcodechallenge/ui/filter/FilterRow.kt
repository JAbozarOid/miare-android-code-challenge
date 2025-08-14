package ir.miare.androidcodechallenge.ui.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.miare.androidcodechallenge.util.PlayerFilter

@Composable
fun FilterRow(
    selected: PlayerFilter,
    onSelected: (PlayerFilter) -> Unit
) {
    val filters = listOf(
        PlayerFilter.NONE to "None",
        PlayerFilter.TOP_THREE_SCORES to "Top 3 Scores",
        PlayerFilter.AVG_GOALS_PER_MATCH to "Avg Goals/Match",
        PlayerFilter.MOST_GOALS_BY_PLAYER to "Most Goals",
        PlayerFilter.TEAM_AND_LEAGUE_RANKING to "Team & League Rank"
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { (filterType, label) ->
            FilterChip(
                selected = selected == filterType,
                onClick = { onSelected(filterType) },
                label = { Text(label) }
            )
        }
    }
}

