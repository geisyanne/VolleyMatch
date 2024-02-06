package co.geisyanne.meuapp.domain.repository

import androidx.lifecycle.LiveData
import co.geisyanne.meuapp.data.local.entity.PlayerEntity

interface PlayerRepository {

    suspend fun insertPlayer(name: String, positionPlayer: Int?, level: Int?, group: Int?): Long

    suspend fun updatePlayer(id: Long, name: String, positionPlayer: Int?, level: Int?, group: Int?)

    suspend fun deletePlayer(id: Long)

    suspend fun deleteSelectedPlayers(ids: List<Long>)

    fun getPlayerByName(name: String): LiveData<List<PlayerEntity>>

    fun getAllPlayers(): LiveData<List<PlayerEntity>>

}