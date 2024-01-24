package co.geisyanne.meuapp.presentation.drawTeams.player

import co.geisyanne.meuapp.presentation.common.base.BasePresenter
import co.geisyanne.meuapp.presentation.common.base.BaseView
import co.geisyanne.meuapp.domain.model.Group

interface RegisterPlayer {

    interface View : BaseView<Presenter> {
        fun showProgress(enabled: Boolean)
        fun onRegisterFailure(message: String)
        fun onRegisterSuccess()

    }

    interface Presenter : BasePresenter {
        suspend fun insertPlayer(name: String, position: Int?, level: Int?, group: Group?)

        fun updatePlayer(name: String, position: Int?, level: Int?, group: Group?)

        fun deletePlayer(id: Long)

        fun deleteSelectedPlayers(ids: List<Long>)

        fun getPlayerByName(name: String)

        fun getAllPlayers()

    }

}