package co.geisyanne.meuapp.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.geisyanne.meuapp.databinding.ActivityMainBinding
import co.geisyanne.meuapp.drawTeams.home.view.DrawTeamsActivity
import co.geisyanne.meuapp.scoreboard.view.ScoreboardActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with (binding) {
            mainBtnScoreboard.setOnClickListener {
                val intent = Intent(baseContext, ScoreboardActivity::class.java)
                startActivity(intent)
            }

            mainBtnDrawTeam.setOnClickListener {
                val intent = Intent(baseContext, DrawTeamsActivity::class.java)
                startActivity(intent)
            }
        }



    }


}