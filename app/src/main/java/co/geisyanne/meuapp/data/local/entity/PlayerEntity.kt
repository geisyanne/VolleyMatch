package co.geisyanne.meuapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.geisyanne.meuapp.domain.model.Group

@Entity(tableName = "player")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    @ColumnInfo(name = "name") val name: String,

    @ColumnInfo(name = "position") val position: Int?,

    @ColumnInfo(name = "level") val level: Int?,

    @ColumnInfo(name = "group") val group: Int?,


    )