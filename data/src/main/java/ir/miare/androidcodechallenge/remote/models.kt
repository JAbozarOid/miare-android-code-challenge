package ir.miare.androidcodechallenge.remote

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class FakeData(
    @JsonProperty("league") var league: League,
    @JsonProperty("players") var players: List<PlayerDto>
)

data class League(
    @JsonProperty("name") val name: String,
    @JsonProperty("country") val country: String,
    @JsonProperty("rank") val rank: Int,
    @JsonProperty("total_matches") val totalMatches: Int,
)

data class PlayerDto(
    @JsonProperty("name") val name: String,
    @JsonProperty("team") val team: TeamDto,
    @JsonProperty("total_goal") val totalGoal: Int
) : Serializable

data class TeamDto(
    @JsonProperty("name") val name: String,
    @JsonProperty("rank") val rank: Int
) : Serializable
