package co.geisyanne.meuapp.drawTeams.register.data

import co.geisyanne.meuapp.common.model.Group

interface RegisterDataSource {

    fun createPlayer(name: String, position: String?, level: Int?, group: Group?, callback: RegisterCallback) {



    }

}