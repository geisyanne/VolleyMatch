package co.geisyanne.meuapp.presentation.drawTeams.player.model

import co.geisyanne.meuapp.presentation.common.base.RegisterCallback
import co.geisyanne.meuapp.domain.model.Group

class PlayerRepository(private val dataSource: PlayerDataSource)  {

    suspend fun insertPlayer(name: String, position: Int?, level: Int?, group: Group?, callback: RegisterCallback) {
        dataSource.insertPlayer(name, position, level, group, callback)
    }

    suspend fun updatePlayer(id: Long, name: String, position: Int?, level: Int?, group: Group?) {
        dataSource.updatePlayer(id, name, position, level, group)
    }

    suspend fun deletePlayer(id: Long) {
        dataSource.deletePlayer(id)
    }

    suspend fun deleteAllPlayers(ids: List<Long>) {
        dataSource.deleteSelectedPlayers(ids)
    }

    suspend fun getPlayerByName(name: String) {
        dataSource.getPlayerByName(name)
    }

    fun getAllPlayers() {
        dataSource.getAllPlayers()
    }




}