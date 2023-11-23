package co.geisyanne.meuapp.drawTeams.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.ActivityDrawTeamsBinding

class DrawTeamsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrawTeamsBinding

    //    private lateinit var playersFragment: PlayersFragment
    private lateinit var playerRegisterFragment: PlayerRegisterFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDrawTeamsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* playersFragment = PlayersFragment()
        supportFragmentManager.beginTransaction().apply {
            add(R.id.drawTeam_container_fragment, playersFragment)
            commit()
        } */


        playerRegisterFragment = PlayerRegisterFragment()
        supportFragmentManager.beginTransaction().apply {
            add(R.id.drawTeam_container_fragment, playerRegisterFragment)
            commit()
        }
    }


}
