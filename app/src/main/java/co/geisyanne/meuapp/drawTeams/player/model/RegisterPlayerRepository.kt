package co.geisyanne.meuapp.drawTeams.player.model

import co.geisyanne.meuapp.common.model.Group

class RegisterPlayerRepository(private val dataSource: PlayerDataSource)  {

    suspend fun upsertPlayer(name: String, position: String?, level: Int?, group: Group?) {
        dataSource.upsertPlayer(name, position, level, group)
    }

    suspend fun deletePlayer() {
        dataSource.deletePlayer()
    }

    suspend fun getPlayerByName(name: String) {
        dataSource.getPlayerByName(name)
    }

    fun getAllPlayer() {
        dataSource.getAllPlayer()
    }




}