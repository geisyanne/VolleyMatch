package co.geisyanne.volleymatch.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "player_table")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val playerId: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "position") val positionPlayer: Int =0,
    @ColumnInfo(name = "level") val level: Int =0,

    ) : Parcelable {
    @IgnoredOnParcel
    @Ignore var selected: Boolean = false
}


