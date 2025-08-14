package ir.miare.androidcodechallenge.model

data class Player(
    val id: String,
    val name: String,
    val teamName: String,
    val teamRank: Int,
    val totalGoal: Int,
    val leagueName: String,
    val leagueCountry: String,
    val leagueRank : Int = 0,
    val totalMatches : Int = 0
)
