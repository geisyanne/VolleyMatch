package co.geisyanne.meuapp.scoreboard.view

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.ActivityScoreboardBinding
import co.geisyanne.meuapp.scoreboard.Scoreboard
import co.geisyanne.meuapp.scoreboard.presenter.ScoreboardPresenter

class ScoreboardActivity : AppCompatActivity(), Scoreboard.View {

    private lateinit var binding: ActivityScoreboardBinding
    override lateinit var presenter: Scoreboard.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE;

        binding = ActivityScoreboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = ScoreboardPresenter(this)

        with(binding) {

            scoreboardTxtScore1.setOnClickListener {
                presenter.increaseScore(1)
            }

            scoreboardTxtScore2.setOnClickListener {
                presenter.increaseScore(2)
            }

            scoreboardTxtScore1.setOnLongClickListener {
                presenter.decreaseScore(1)
                true
            }

            scoreboardTxtScore2.setOnLongClickListener {
                presenter.decreaseScore(2)
                true
            }

            scoreboardBtnRestart.setOnClickListener {
                presenter.restartScore()
            }

            scoreboardBtnHelp.setOnClickListener {
                displayHelp()
            }

        }

    }

    override fun updateScore(team: Int, score: Int) {
        val textView = when (team) {
            1 -> binding.scoreboardTxtScore1
            2 -> binding.scoreboardTxtScore2
            else -> throw IllegalArgumentException("Equipe inválida: $team")
        }
        textView.text = getString(R.string.txt_score, score)
    }

    override fun displayHelp() {
        val alertDialog = AlertDialog.Builder(this@ScoreboardActivity)

        alertDialog.apply {
            setMessage(R.string.txt_help)
            setNeutralButton(android.R.string.ok, null)
        }
            .create()
            .show()
    }




    /*with(binding) {

        scoreboardTxtScore1.setOnClickListener {
            score1++
            scoreboardTxtScore1.text = getString(R.string.txt_score, score1)
        }

        scoreboardTxtScore1.setOnLongClickListener {
            if (score1 > 0) {
                score1--
                scoreboardTxtScore1.text = getString(R.string.txt_score, score1)
            }
            true
        }

        scoreboardTxtScore2.setOnClickListener {
            score2++
            scoreboardTxtScore2.text = getString(R.string.txt_score, score2)
        }

        scoreboardTxtScore2.setOnLongClickListener {
            if (score2 > 0) {
                score2--
                scoreboardTxtScore2.text = getString(R.string.txt_score, score2)
            }
            true
        }

        scoreboardBtnRestart.setOnClickListener {
            score1 = 0
            scoreboardTxtScore1.text = getString(R.string.txt_score, score1)
            score2 = 0
            scoreboardTxtScore2.text = getString(R.string.txt_score, score2)
        }

        scoreboardBtnHelp.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this@ScoreboardActivity)

            alertDialog.apply {
                setMessage(R.string.txt_help)
                setNeutralButton(android.R.string.ok, null)
            }
                .create()
                .show()
        }

    }*/


    }




