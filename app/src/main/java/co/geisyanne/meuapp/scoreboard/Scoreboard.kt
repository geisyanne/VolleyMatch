package co.geisyanne.meuapp.scoreboard

interface Scoreboard {

    interface View {
        fun increaseAndDecreaseScore(currentScore: Int)
        fun restartScore(currentScore: Int)
        fun displayHelp()
    }

}