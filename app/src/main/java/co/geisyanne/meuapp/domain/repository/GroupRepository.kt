package co.geisyanne.meuapp.domain.repository

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import co.geisyanne.meuapp.data.local.entity.GroupEntity
import co.geisyanne.meuapp.data.local.entity.GroupPlayerCrossRef
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.data.local.relation.GroupWithPlayers

interface GroupRepository {

    suspend fun insertGroup(name: String): Long

    suspend fun updateGroup(id: Long, name: String)

    suspend fun deleteGroup(id: Long)

    suspend fun deleteSelectedGroups(ids: List<Long>)

    fun getGroupByName(name: String): LiveData<List<GroupEntity>>

    fun getAllGroups(): LiveData<List<GroupEntity>>

    // RELATIONSHIP GROUP-PLAYER

    suspend fun insertRelation(groupId: Long, playerId: Long)

    suspend fun deleteRelation(groupId: Long, playerId: Long)

    fun getPlayersInGroup(groupId: Long): LiveData<List<GroupWithPlayers>>

}