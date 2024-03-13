package co.geisyanne.volleymatch.data.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import co.geisyanne.volleymatch.data.local.entity.GroupEntity
import co.geisyanne.volleymatch.data.local.entity.GroupPlayerCrossRef
import co.geisyanne.volleymatch.data.local.entity.PlayerEntity

data class GroupWithPlayers(
    @Embedded val group: GroupEntity,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "playerId",
        associateBy = Junction(GroupPlayerCrossRef::class)
    )
    val playerList: List<PlayerEntity>
)