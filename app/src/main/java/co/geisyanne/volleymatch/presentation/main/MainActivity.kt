package co.geisyanne.volleymatch.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import co.geisyanne.volleymatch.databinding.ActivityMainBinding
import co.geisyanne.volleymatch.presentation.drawTeams.home.HomeDrawTeamsActivity
import co.geisyanne.volleymatch.presentation.scoreboard.ScoreboardActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            mainBtnScoreboard.setOnClickListener {
                startActivity(Intent(baseContext, ScoreboardActivity::class.java))
            }

            mainBtnDrawTeam.setOnClickListener {
                startActivity(Intent(baseContext, HomeDrawTeamsActivity::class.java))
            }
        }
    }
}