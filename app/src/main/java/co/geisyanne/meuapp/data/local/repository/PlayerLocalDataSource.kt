package co.geisyanne.meuapp.data.local.repository

import androidx.lifecycle.LiveData
import co.geisyanne.meuapp.data.local.dao.PlayerDao
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.domain.repository.PlayerRepository

class PlayerLocalDataSource(
    private val playerDao: PlayerDao
) : PlayerRepository {

    override suspend fun insertPlayer(
        name: String,
        positionPlayer: Int?,
        level: Int?,
        group: Int?,
    ): Long {
        val player = PlayerEntity(
            name = name,
            positionPlayer = positionPlayer,
            level = level,
            group = group
        )
        return playerDao.insertPlayer(player)
    }

    override suspend fun updatePlayer(
        id: Long,
        name: String,
        positionPlayer: Int?,
        level: Int?,
        group: Int?
    ) {
        val player = PlayerEntity(
            id = id,
            name = name,
            positionPlayer = positionPlayer,
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

    override fun getPlayerByName(name: String): LiveData<List<PlayerEntity>> {
        return playerDao.getPlayerByName(name)
    }

    override fun getAllPlayers(): LiveData<List<PlayerEntity>> {
        return playerDao.getAllPlayers()
    }


}