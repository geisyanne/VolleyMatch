package co.geisyanne.meuapp.presentation.scoreboard

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.ActivityScoreboardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ScoreboardActivity : AppCompatActivity() {


    private var binding: ActivityScoreboardBinding? = null
    private lateinit var viewModel: ScoreboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE

        binding = ActivityScoreboardBinding.inflate(layoutInflater)

        setContentView(binding?.root)

        viewModel = ViewModelProvider(this)[ScoreboardViewModel::class.java]

        binding?.let {
            with(it) {

                scoreboardTxtScore1.setOnClickListener {
                    viewModel.increaseScore(1)
                    updateScore(1)
                }

                scoreboardTxtScore2.setOnClickListener {
                    viewModel.increaseScore(2)
                    updateScore(2)
                }

                scoreboardTxtScore1.setOnLongClickListener {
                    viewModel.decreaseScore(1)
                    updateScore(1)
                    true
                }

                scoreboardTxtScore2.setOnLongClickListener {
                    viewModel.decreaseScore(2)
                    updateScore(2)
                    true
                }

                scoreboardBtnRestart.setOnClickListener {
                    viewModel.restartScore()
                    restartScore()

                }

                scoreboardBtnHelp.setOnClickListener {
                    displayHelp()
                }
            }
        }

    }

    private fun updateScore(team: Int) {
        val score = when (team) {
            1 -> viewModel.getCurrentScore1()
            2 -> viewModel.getCurrentScore2()
            else -> throw IllegalArgumentException("Invalid team: $team")
        }

        val textView = when (team) {
            1 -> binding?.scoreboardTxtScore1
            2 -> binding?.scoreboardTxtScore2
            else -> throw IllegalArgumentException("Invalid team: $team")
        }
        textView?.text = getString(R.string.txt_score, score)
    }

    private fun restartScore() {
        binding?.scoreboardTxtScore1?.text = getString(R.string.txt_score, 0)
        binding?.scoreboardTxtScore2?.text = getString(R.string.txt_score, 0)
    }

    private fun displayHelp() {
        val builder = MaterialAlertDialogBuilder(this@ScoreboardActivity)

        builder.setMessage(R.string.txt_help)
            .setNeutralButton(android.R.string.ok, null)
        builder.create().show()
    }

    @Suppress("DEPRECATION")
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
        super.onDestroy()
    }

}




