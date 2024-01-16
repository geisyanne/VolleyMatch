package co.geisyanne.meuapp.drawTeams.player.presenter

import co.geisyanne.meuapp.drawTeams.player.Players

class PlayerListPresenter(
    private var view: Players.View?,
) : Players.Presenter {


    override fun onDestroy() {
        view = null
    }


}