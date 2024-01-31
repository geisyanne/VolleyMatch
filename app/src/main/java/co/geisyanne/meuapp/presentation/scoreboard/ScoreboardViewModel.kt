package co.geisyanne.meuapp.presentation.scoreboard


import androidx.lifecycle.ViewModel

class ScoreboardViewModel : ViewModel() {

    private var currentScore1 = 0
    private var currentScore2 = 0

    fun getCurrentScore1(): Int = currentScore1
    fun getCurrentScore2(): Int = currentScore2

    fun increaseScore(team: Int) {
        when (team) {
            1 -> ++currentScore1
            2 -> ++currentScore2
            else -> throw IllegalArgumentException("Invalid team: $team")
        }
    }

    fun decreaseScore(team: Int) {
        when (team) {
            1 -> if (currentScore1 > 0) --currentScore1
            2 -> if (currentScore2 > 0) --currentScore2
            else -> throw IllegalArgumentException("Invalid team: $team")
        }

    }

    fun restartScore() {
        currentScore1 = 0
        currentScore2 = 0
    }

}