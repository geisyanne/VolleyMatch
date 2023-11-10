package co.geisyanne.meuapp.main.view

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.ActivityMainBinding
import co.geisyanne.meuapp.drawTeam.DrawTeamActivity
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
                val intent = Intent(baseContext, DrawTeamActivity::class.java)
                startActivity(intent)
            }
        }



    }


}