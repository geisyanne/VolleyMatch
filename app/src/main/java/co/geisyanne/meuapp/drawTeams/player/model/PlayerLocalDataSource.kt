package co.geisyanne.meuapp.drawTeams.player.model

import co.geisyanne.meuapp.common.model.Group
import co.geisyanne.meuapp.data.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

class PlayerLocalDataSource : PlayerDataSource {

    override suspend fun upsertPlayer(
        name: String,
        position: String?,
        level: Int?,
        group: Group?
    ): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlayer() {
        TODO("Not yet implemented")
    }
    override suspend fun getPlayerByName(name: String): PlayerEntity? {
        TODO("Not yet implemented")
    }

    override fun getAllPlayer(): Flow<List<PlayerEntity>> {
        TODO("Not yet implemented")
    }



}