package ir.miare.androidcodechallenge.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followed_players")
data class FollowedPlayerEntity(@PrimaryKey val playerId: String)
