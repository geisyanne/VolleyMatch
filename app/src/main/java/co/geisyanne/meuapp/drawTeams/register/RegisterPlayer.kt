package co.geisyanne.meuapp.drawTeams.register

import co.geisyanne.meuapp.common.base.BasePresenter
import co.geisyanne.meuapp.common.base.BaseView
import co.geisyanne.meuapp.common.model.Group

interface RegisterPlayer {

    interface View : BaseView<Presenter> {
        fun showProgress(enabled: Boolean)
        fun onRegisterFailure(message: String)
        fun onRegisterSuccess()

    }

    interface Presenter : BasePresenter {
        fun createPlayer(name: String, position: String?, level: Int?, group: Group?)

    }

    interface Repository {
        fun createPlayer()
        fun createGroup()
        fun updatePlayer()
        fun updateGroup()

    }


}