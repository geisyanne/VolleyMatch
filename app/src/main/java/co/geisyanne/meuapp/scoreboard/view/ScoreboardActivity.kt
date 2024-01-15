package co.geisyanne.meuapp.scoreboard.view

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.ActivityScoreboardBinding
import co.geisyanne.meuapp.scoreboard.Scoreboard
import co.geisyanne.meuapp.scoreboard.presenter.ScoreboardPresenter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ScoreboardActivity : AppCompatActivity(), Scoreboard.View {

    private var binding: ActivityScoreboardBinding? = null
    override lateinit var presenter: Scoreboard.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE

        binding = ActivityScoreboardBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        presenter = ScoreboardPresenter(this)

        binding?.let {
            with(it) {

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

    }

    override fun updateScore(team: Int, score: Int) {
        val textView = when (team) {
            1 -> binding?.scoreboardTxtScore1
            2 -> binding?.scoreboardTxtScore2
            else -> throw IllegalArgumentException("Invalid team: $team")
        }
        textView?.text = getString(R.string.txt_score, score)
    }

    override fun displayHelp() {
        val builder = MaterialAlertDialogBuilder(this@ScoreboardActivity)

        builder.setMessage(R.string.txt_help)
            .setNeutralButton(android.R.string.ok, null)
        builder.create().show()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val decorView = window.decorView
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                decorView.windowInsetsController?.hide(WindowInsets.Type.systemBars())
            } else {
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
        }
    }

    override fun onDestroy() {
        binding = null
        presenter.onDestroy()
        super.onDestroy()
    }

}




