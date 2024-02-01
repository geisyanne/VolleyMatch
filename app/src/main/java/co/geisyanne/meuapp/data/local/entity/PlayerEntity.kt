package co.geisyanne.meuapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "player")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    @ColumnInfo(name = "name") val name: String,

    @ColumnInfo(name = "position") val positionPlayer: Int?,

    @ColumnInfo(name = "level") val level: Int?,

    @ColumnInfo(name = "group") val group: Int?

) : Parcelable