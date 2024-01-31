package co.geisyanne.meuapp.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.geisyanne.meuapp.databinding.ActivityMainBinding
import co.geisyanne.meuapp.presentation.drawTeams.home.HomeDrawTeamsActivity
import co.geisyanne.meuapp.presentation.scoreboard.ScoreboardActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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