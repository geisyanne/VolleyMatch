package co.geisyanne.meuapp.presentation.drawTeams.player.model

import co.geisyanne.meuapp.domain.model.Group
import co.geisyanne.meuapp.data.local.dao.PlayerDao
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.presentation.common.base.RegisterCallback
import kotlinx.coroutines.flow.Flow

class PlayerLocalDataSource(
    private val playerDao: PlayerDao
) : PlayerDataSource {

    override suspend fun insertPlayer(
        name: String,
        position: Int?,
        level: Int?,
        group: Group?,
        callback: RegisterCallback
    ): Long {
        val player = PlayerEntity(
            name = name,
            position = position,
            level = level,
            group = group
        )
        return playerDao.insertPlayer(player)
    }

    override suspend fun updatePlayer(
        id: Long,
        name: String,
        position: Int?,
        level: Int?,
        group: Group?
    ) {
        val player = PlayerEntity(
            id = id,
            name = name,
            position = position,
            level = level,
            group = group
        )

        playerDao.updatePlayer(player)
    }

    override suspend fun deletePlayer(id: Long) {
        playerDao.deletePlayer(id)
    }
    override suspend fun deleteSelectedPlayers(ids: List<Long>) {
        playerDao.deleteSelectedPlayers(ids)
    }

    override suspend fun getPlayerByName(name: String): List<PlayerEntity?> {
        return playerDao.getPlayerByName(name)
    }

    override fun getAllPlayers(): Flow<List<PlayerEntity>> {
        return playerDao.getAllPlayers()
    }


}