package ir.miare.androidcodechallenge.model

data class LeagueWithPlayers(
    val league: League,
    val players: List<Player>,
    val avgGoalsPerMatch: Double = 0.0
)
