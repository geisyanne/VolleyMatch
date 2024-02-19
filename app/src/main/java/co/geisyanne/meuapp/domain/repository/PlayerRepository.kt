package co.geisyanne.meuapp.domain.repository

import androidx.lifecycle.LiveData
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.data.local.relation.GroupWithPlayers
import co.geisyanne.meuapp.data.local.relation.PlayerWithGroups

interface PlayerRepository {

    suspend fun insertPlayer(name: String, positionPlayer: Int?, level: Int?): Long

    suspend fun updatePlayer(id: Long, name: String, positionPlayer: Int?, level: Int?)

    suspend fun deletePlayer(id: Long)

    suspend fun deleteSelectedPlayers(ids: List<Long>)

    fun getPlayerByName(name: String): LiveData<List<PlayerEntity>>

    fun getAllPlayers(): LiveData<List<PlayerEntity>>

    fun getGroupsForPlayer(playerId: Long): LiveData<List<PlayerWithGroups>>

}