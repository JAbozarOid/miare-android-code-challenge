package ir.miare.androidcodechallenge.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.miare.androidcodechallenge.model.Team

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val teamName: String,
    val teamRank: Int,
    val totalGoal: Int,
    val leagueName: String,
    val leagueCountry: String,
    val leagueRank : Int,
    val totalMatches : Int,
)
