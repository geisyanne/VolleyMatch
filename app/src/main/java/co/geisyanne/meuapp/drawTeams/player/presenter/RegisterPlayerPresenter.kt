package co.geisyanne.meuapp.drawTeams.player.presenter

import co.geisyanne.meuapp.common.model.Group
import co.geisyanne.meuapp.drawTeams.player.RegisterPlayer

class RegisterPlayerPresenter(
    private var view: RegisterPlayer.View?,
    // private val repository: RegisterRepository
) : RegisterPlayer.Presenter {
    override fun createPlayer(name: String, position: String?, level: Int?, group: Group?) {

        view?.showProgress(true)

       /* repository.createPlayer(name, position, level, group, object : RegisterCallback {
            override fun onSuccess() {
                view?.onRegisterSuccess() // TEMPORARIA
            }

            override fun onFailure(message: String) {
                view?.onRegisterFailure(message)
            }

            override fun onComplete() {
                view?.showProgress(false)
            }
        })*/

    }

    override fun onDestroy() {
        view = null
    }

}


