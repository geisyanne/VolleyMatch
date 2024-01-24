package co.geisyanne.meuapp.presentation.drawTeams.player.presenter

import co.geisyanne.meuapp.presentation.common.base.RegisterCallback
import co.geisyanne.meuapp.domain.model.Group
import co.geisyanne.meuapp.presentation.drawTeams.player.RegisterPlayer
import co.geisyanne.meuapp.presentation.drawTeams.player.model.PlayerRepository

class RegisterPlayerPresenter(
    private var view: RegisterPlayer.View?,
    private val repository: PlayerRepository
) : RegisterPlayer.Presenter {

    override suspend fun insertPlayer(name: String, position: Int?, level: Int?, group: Group?) {

        view?.showProgress(true)

        repository.insertPlayer(name, position, level, group, object : RegisterCallback {
            override fun onSuccess() {
                view?.onRegisterSuccess()
            }

            override fun onFailure(message: String) {
                view?.onRegisterFailure(message)
            }

            override fun onComplete() {
               view?.showProgress(false)
            }


        })




    // PASSAR PARA VIEW O SUCESSO OU A FALHA

    }

    override fun updatePlayer(name: String, position: Int?, level: Int?, group: Group?) {
        TODO("Not yet implemented")
    }

    override fun deletePlayer(id: Long) {
        TODO("Not yet implemented")
    }

    override fun deleteSelectedPlayers(ids: List<Long>) {
        TODO("Not yet implemented")
    }

    override fun getPlayerByName(name: String) {
        TODO("Not yet implemented")
    }

    override fun getAllPlayers() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        view = null
    }

}


