package co.geisyanne.meuapp.drawTeams.players.presenter

import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.drawTeams.players.Players
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PlayersPresenter(
    private var view: Players.View?,
) : Players.Presenter {


    override fun onDestroy() {
        view = null
    }


}