package co.geisyanne.meuapp.drawTeams.home.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.blue
import androidx.fragment.app.Fragment
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.ActivityDrawTeamsBinding
import co.geisyanne.meuapp.drawTeams.draw.DrawFragment
import co.geisyanne.meuapp.drawTeams.groups.GroupListFragment
import co.geisyanne.meuapp.drawTeams.players.PlayersFragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.MaterialShapeDrawable

class DrawTeamsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrawTeamsBinding

    private lateinit var playersFragment: PlayersFragment
    private lateinit var drawFragment: DrawFragment
    private lateinit var groupListFragment: GroupListFragment
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDrawTeamsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        playersFragment = PlayersFragment()
        drawFragment = DrawFragment()
        groupListFragment = GroupListFragment()


        binding.drawTeamBottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_bottom_player -> {
                    showFragment(playersFragment)
                    setToolbarTitle(R.string.toolbar_title_players)
                    true
                }

                R.id.menu_bottom_draw_times -> {
                    showFragment(drawFragment)
                    setToolbarTitle(R.string.toolbar_title_drawTeams)
                    true
                }

                R.id.menu_bottom_groups -> {
                    showFragment(groupListFragment)
                    setToolbarTitle(R.string.toolbar_title_groups)
                    true
                }

                else -> throw IllegalArgumentException("Menu item desconhecido: ${menuItem.itemId}")
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        
        return super.onCreateOptionsMenu(menu)
    }


    private fun setToolbarTitle(@StringRes resId: Int) {
        binding.mainToolbar.setTitle(resId)
    }


    private fun showFragment(fragment: Fragment) {
        if (currentFragment !== fragment) {
            replaceFragment(fragment)
            currentFragment = fragment
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.drawTeam_container_fragment, fragment)
            .commit()
    }

}
