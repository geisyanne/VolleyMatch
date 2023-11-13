package co.geisyanne.meuapp.scoreboard

import android.widget.TextView
import co.geisyanne.meuapp.common.base.BasePresenter
import co.geisyanne.meuapp.common.base.BaseView

interface Scoreboard {

    interface View : BaseView<Presenter> {
        fun updateScore(team: Int, score: Int)
        fun displayHelp()
    }

    interface Presenter : BasePresenter {
        fun increaseScore(team: Int)
        fun decreaseScore(team: Int)
        fun restartScore()
    }


}