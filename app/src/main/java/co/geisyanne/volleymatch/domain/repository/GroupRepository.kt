package co.geisyanne.volleymatch.domain.repository

import androidx.lifecycle.LiveData
import co.geisyanne.volleymatch.data.local.entity.GroupEntity
import co.geisyanne.volleymatch.data.local.relation.GroupWithPlayers

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