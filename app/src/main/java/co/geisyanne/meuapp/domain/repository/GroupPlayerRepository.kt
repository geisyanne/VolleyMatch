package co.geisyanne.meuapp.domain.repository

import androidx.lifecycle.LiveData
import co.geisyanne.meuapp.data.local.entity.GroupEntity
import co.geisyanne.meuapp.data.local.entity.PlayerEntity

interface GroupPlayerRepository {

    suspend fun insert(groupId: Long, playerId: Long): Long

    suspend fun delete(id: Long)

    fun getPlayersInGroup(groupId: Long): LiveData<List<PlayerEntity>>?

    fun getGroupsForPlayer(playerId: Long): LiveData<List<GroupEntity>>?

}