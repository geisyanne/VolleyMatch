package co.geisyanne.meuapp.drawTeams.register.data

import co.geisyanne.meuapp.common.model.Group
import co.geisyanne.meuapp.common.model.Player

class RegisterRepository(private val dataSource: RegisterDataSource)  {

    fun createPlayer(name: String, position: String?, level: Int?, group: Group?, callback: RegisterCallback) {
        dataSource.createPlayer(name, position, level, group, callback)
    }

    fun createGroup(name: String, players: List<Player>?, selected: Boolean = false) {

    }

    fun updatePlayer(name: String, position: String?, level: Int?, group: Group?) {

    }

    fun updateGroup(name: String, players: List<Player>?, selected: Boolean = false) {

    }

}