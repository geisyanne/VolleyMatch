package co.geisyanne.meuapp.drawTeams.player.model

import co.geisyanne.meuapp.common.model.Group
import co.geisyanne.meuapp.data.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

interface PlayerDataSource {

    suspend fun upsertPlayer(name: String, position: String?, level: Int?, group: Group?): Long

    suspend fun deletePlayer()

    suspend fun getPlayerByName(name: String): PlayerEntity?

    fun getAllPlayer(): Flow<List<PlayerEntity>>




}