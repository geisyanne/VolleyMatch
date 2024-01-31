package co.geisyanne.meuapp.presentation.drawTeams.home

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.databinding.ActivityDrawTeamsBinding
import co.geisyanne.meuapp.presentation.drawTeams.draw.DrawFragment
import co.geisyanne.meuapp.presentation.drawTeams.group.GroupListFragment
import co.geisyanne.meuapp.presentation.drawTeams.player.list.PlayerListFragment
import co.geisyanne.meuapp.presentation.drawTeams.player.register.RegisterPlayerFragment

class HomeDrawTeamsActivity : AppCompatActivity(), FragmentAttachListener {

    private var binding: ActivityDrawTeamsBinding? = null

    private lateinit var playerListFragment: PlayerListFragment
    private lateinit var drawFragment: DrawFragment
    private lateinit var groupListFragment: GroupListFragment
    private lateinit var homeDrawTeamsFragment: HomeDrawTeamsFragment
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDrawTeamsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        playerListFragment = PlayerListFragment()
        drawFragment = DrawFragment()
        groupListFragment = GroupListFragment()
        homeDrawTeamsFragment = HomeDrawTeamsFragment()

        replaceFragment(homeDrawTeamsFragment)
        binding?.drawTeamBottomNav?.selectedItemId =
            R.id.invisible // NO PRE-SELECTED BOTTOM NAVIGATION

        binding?.drawTeamBottomNav?.setOnItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.menu_bottom_groups -> {
                    showFragment(groupListFragment)
                    setToolbarTitle(R.string.toolbar_title_groups)
                    true
                }

                R.id.menu_bottom_player -> {
                    showFragment(playerListFragment)
                    setToolbarTitle(R.string.toolbar_title_players)
                    true
                }

                R.id.menu_bottom_draw_times -> {
                    showFragment(drawFragment)
                    setToolbarTitle(R.string.toolbar_title_drawTeams)
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
        binding?.mainToolbar?.setTitle(resId)
    }

    private fun showFragment(fragment: Fragment) {
        if (currentFragment !== fragment) {
            replaceFragment(fragment)
            currentFragment = fragment
        }
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false, tag: String? = null) {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.drawTeam_container_fragment, fragment, tag)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    override fun goToRegisterPlayer() {
        val fragment = RegisterPlayerFragment()
        replaceFragment(fragment, addToBackStack = true, "RegisterPlayerTag")
        binding?.drawTeamBottomNav?.visibility = View.GONE
    }

    override fun goToUpdatePlayer(player: PlayerEntity?) {
        val bundle = Bundle().apply {
            putParcelable("KEY_PLAYER", player)
        }
        val fragment = RegisterPlayerFragment()
        fragment.arguments = bundle
        replaceFragment(fragment, addToBackStack = true, "UpdatePlayerTag")
        binding?.drawTeamBottomNav?.visibility = View.GONE
    }


    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}
