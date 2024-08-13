package co.geisyanne.volleymatch.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "team_table")
data class TeamEntity(
    @PrimaryKey(autoGenerate = true) val teamId: Long = 0,
    @ColumnInfo(name = "number") val num: Int,
    @ColumnInfo(name = "players") val playerList: List<PlayerEntity>
) : Parcelable
