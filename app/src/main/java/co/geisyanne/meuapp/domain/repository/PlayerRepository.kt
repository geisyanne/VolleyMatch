package co.geisyanne.meuapp.domain.repository

import androidx.lifecycle.LiveData
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.presentation.common.base.RegisterCallback
import co.geisyanne.meuapp.domain.model.Group

interface PlayerRepository {

    suspend fun insertPlayer(name: String, position: Int?, level: Int?, group: Int?): Long

    suspend fun updatePlayer(id: Long, name: String, position: Int?, level: Int?, group: Int?)

    suspend fun deletePlayer(id: Long)

    suspend fun deleteSelectedPlayers(ids: List<Long>)

    suspend fun getPlayerByName(name: String): List<PlayerEntity?>

    suspend fun getAllPlayers(): LiveData<List<PlayerEntity>>

}