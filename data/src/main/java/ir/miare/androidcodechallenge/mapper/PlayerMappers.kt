package ir.miare.androidcodechallenge.mapper

import ir.miare.androidcodechallenge.local.PlayerEntity
import ir.miare.androidcodechallenge.model.Player
import ir.miare.androidcodechallenge.model.Team
import ir.miare.androidcodechallenge.remote.PlayerDto

fun PlayerDto.toPlayerEntity(leagueName: String, leagueCountry: String): PlayerEntity {
    val id = generateId(leagueName, name)
    return PlayerEntity(
        id = id,
        name = name,
        teamName = team.name,
        teamRank = team.rank,
        totalGoal = totalGoal,
        leagueName = leagueName,
        leagueCountry = leagueCountry,
        leagueRank = 0,
        totalMatches = 0,
    )
}

fun PlayerEntity.toDomain(): Player {
    return Player(
        id = id,
        name = name,
        teamName = teamName,
        teamRank = teamRank,
        totalGoal = totalGoal,
        leagueName = leagueName,
        leagueCountry = leagueCountry,
        leagueRank = leagueRank,
        totalMatches = totalMatches,
    )
}

fun generateId(league: String, playerName: String): String {
    return (league + "_" + playerName).replace("\\s+".toRegex(), "_").lowercase()
}