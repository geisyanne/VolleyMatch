package co.geisyanne.meuapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import co.geisyanne.meuapp.common.model.Group

@Entity(tableName = "player")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "name") val name: String,

    @ColumnInfo(name = "position") val position: String?,

    @ColumnInfo(name = "level") val level: Int?,

    @ColumnInfo(name = "group") val group: Group?,


)