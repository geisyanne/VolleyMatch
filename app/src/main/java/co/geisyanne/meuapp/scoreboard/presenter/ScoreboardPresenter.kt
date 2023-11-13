package co.geisyanne.meuapp.scoreboard.presenter

import co.geisyanne.meuapp.scoreboard.Scoreboard

class ScoreboardPresenter(
    private var view: Scoreboard.View?,
) : Scoreboard.Presenter {

    private var currentScore1 = 0
    private var currentScore2 = 0

    override fun increaseScore(team: Int) {
        val score = when (team) {
            1 -> ++currentScore1
            2 -> ++currentScore2
            else -> throw IllegalArgumentException("Equipe inválida: $team")
        }
        view?.updateScore(team, score)
    }

    override fun decreaseScore(team: Int) {
        val score = when (team) {
            1 -> if (currentScore1 > 0) --currentScore1 else currentScore1
            2 -> if (currentScore2 > 0) --currentScore2 else currentScore2
            else -> throw IllegalArgumentException("Equipe inválida: $team")
        }
        view?.updateScore(team, score)
    }

    override fun restartScore() {
        currentScore1 = 0
        currentScore2 = 0
        view?.updateScore(1, 0)
        view?.updateScore(2, 0)
    }

    override fun onDestroy() {
        view = null
    }

}

