package co.geisyanne.meuapp.scoreboard.view

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.ActivityScoreboardBinding

class ScoreboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE;

        binding = ActivityScoreboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var score1 = 0
        var score2 = 0

        with(binding) {

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

        }


    }
}