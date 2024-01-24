package co.geisyanne.meuapp.presentation.drawTeams.player.presenter

import co.geisyanne.meuapp.presentation.drawTeams.player.PlayerList

class PlayerListPresenter(
    private var view: PlayerList.View?,
) : PlayerList.Presenter {


    override fun onDestroy() {
        view = null
    }


}