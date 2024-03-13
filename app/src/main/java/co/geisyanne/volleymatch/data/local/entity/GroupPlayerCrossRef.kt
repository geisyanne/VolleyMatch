package co.geisyanne.volleymatch.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(primaryKeys = ["groupId", "playerId"])
data class GroupPlayerCrossRef (
    @ColumnInfo(name = "groupId") val groupId: Long,
    @ColumnInfo(name = "playerId") val playerId: Long
)

