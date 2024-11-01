package co.geisyanne.volleymatch.presentation.scoreboard

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.databinding.ActivityScoreboardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class ScoreboardActivity : AppCompatActivity() {

    private var binding: ActivityScoreboardBinding? = null
    private val viewModel: ScoreboardViewModel by viewModel()
    private lateinit var powerManager: PowerManager
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScoreboardBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // MATER TELA LIGADA
        powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ON_AFTER_RELEASE,
            "VolleyMatch:WakeLock"
        )
        wakeLock?.acquire(3 * 60 * 60 * 1000L)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setupListeners()
        hideSystemUI()
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        val decorView = window.decorView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Esconde as barras do sistema usando WindowInsetsController (para Android 11+)
            decorView.windowInsetsController?.let { controller ->
                controller.hide(WindowInsets.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Esconde as barras do sistema usando systemUiVisibility (para Android 10 e anteriores)
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    override fun onPause() {
        super.onPause()
        wakeLock?.release()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}




