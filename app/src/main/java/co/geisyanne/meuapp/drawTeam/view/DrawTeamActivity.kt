package co.geisyanne.meuapp.drawTeam.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.ActivityDrawTeamBinding

class DrawTeamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrawTeamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDrawTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }
}