package co.geisyanne.meuapp.presentation.drawTeams.player.model

import co.geisyanne.meuapp.presentation.common.base.RegisterCallback
import co.geisyanne.meuapp.domain.model.Group
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

interface PlayerDataSource {

    suspend fun insertPlayer(name: String, position: Int?, level: Int?, group: Group?, callback: RegisterCallback): Long
    suspend fun updatePlayer(id: Long, name: String, position: Int?, level: Int?, group: Group?)

    suspend fun deletePlayer(id: Long)
    suspend fun deleteSelectedPlayers(ids: List<Long>)

    suspend fun getPlayerByName(name: String): List<PlayerEntity?>

    fun getAllPlayers(): Flow<List<PlayerEntity>>




}