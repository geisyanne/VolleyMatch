package co.geisyanne.volleymatch.domain.repository

import androidx.lifecycle.LiveData
import co.geisyanne.volleymatch.data.local.entity.GroupEntity
import co.geisyanne.volleymatch.data.local.entity.PlayerEntity

interface GroupPlayerRepository {

    suspend fun insert(groupId: Long, playerId: Long): Long

    suspend fun delete(id: Long)

    fun getPlayersInGroup(groupId: Long): LiveData<List<PlayerEntity>>?

    fun getGroupsForPlayer(playerId: Long): LiveData<List<GroupEntity>>?

}