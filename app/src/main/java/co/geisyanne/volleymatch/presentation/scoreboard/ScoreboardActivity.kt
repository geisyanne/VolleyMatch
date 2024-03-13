package co.geisyanne.volleymatch.presentation.scoreboard

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.databinding.ActivityScoreboardBinding
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

        setupListeners()

    }

    private fun setupListeners() {
        binding?.apply {

            scoreboardTxtScore1.setOnClickListener { handlerIncreaseScore(1) }
            scoreboardTxtScore2.setOnClickListener { handlerIncreaseScore(2) }

            scoreboardTxtScore1.setOnLongClickListener { handlerDecreaseScore(1) }
            scoreboardTxtScore2.setOnLongClickListener { handlerDecreaseScore(2) }

            scoreboardMenu.setOnClickListener { showPopupMenu(it) }
        }
    }

    private fun handlerIncreaseScore(team: Int) {
        viewModel.increaseScore(team)
        updateScore(team)
    }

    private fun handlerDecreaseScore(team: Int): Boolean {
        viewModel.decreaseScore(team)
        updateScore(team)
        return true
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


    fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_popup_scoreboard)
        popupMenu.setOnMenuItemClickListener { item ->

            when (item.itemId) {
                R.id.menu_restart -> {
                    handlerRestartScore()
                    true
                }

                R.id.menu_help -> {
                    displayHelp()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun handlerRestartScore() {
        viewModel.restartScore()
        binding?.scoreboardTxtScore1?.text = getString(R.string.txt_score, 0)
        binding?.scoreboardTxtScore2?.text = getString(R.string.txt_score, 0)
    }

    private fun displayHelp() {
        MaterialAlertDialogBuilder(this@ScoreboardActivity)
            .setMessage(R.string.txt_help)
            .setNeutralButton(android.R.string.ok, null)
            .create()
            .show()
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




