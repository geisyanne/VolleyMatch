package co.geisyanne.volleymatch.presentation.drawTeams.home

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.data.local.entity.PlayerEntity
import co.geisyanne.volleymatch.data.local.entity.TeamEntity
import co.geisyanne.volleymatch.databinding.ActivityDrawTeamsBinding
import co.geisyanne.volleymatch.domain.model.Team
import co.geisyanne.volleymatch.presentation.drawTeams.draw.DrawFragment
import co.geisyanne.volleymatch.presentation.drawTeams.player.list.PlayerListFragment
import co.geisyanne.volleymatch.presentation.drawTeams.player.register.RegisterPlayerFragment
import co.geisyanne.volleymatch.presentation.drawTeams.result.EditResultFragment
import co.geisyanne.volleymatch.presentation.drawTeams.result.ResultFragment

class HomeDrawTeamsActivity : AppCompatActivity(), FragmentAttachListener,
    PlayerListFragment.ActionBarTitleUpdater {

    private var binding: ActivityDrawTeamsBinding? = null

    private lateinit var playerListFragment: PlayerListFragment
    private lateinit var drawFragment: DrawFragment

    //private lateinit var groupListFragment: GroupFragment

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDrawTeamsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.mainToolbar)
        supportActionBar?.title = "Jogadores"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        playerListFragment = PlayerListFragment()
        drawFragment = DrawFragment()
        //groupListFragment = GroupFragment()

        replaceFragment(playerListFragment)
        binding?.drawTeamBottomNav?.selectedItemId =
            R.id.menu_bottom_player // NO PRE-SELECTED BOTTOM NAVIGATION

        binding?.drawTeamBottomNav?.setOnItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                /*R.id.menu_bottom_groups -> {
                    showFragment(groupListFragment)
                    setToolbarTitle(R.string.toolbar_title_groups)
                    true
                }*/

                R.id.menu_bottom_player -> {
                    showFragment(playerListFragment)
                    supportActionBar?.title = getString(R.string.toolbar_title_players)
                    true
                }

                R.id.menu_bottom_draw_times -> {
                    showFragment(drawFragment)
                    supportActionBar?.title = getString(R.string.toolbar_title_drawTeams)
                    true
                }

                else -> throw IllegalArgumentException("Menu item desconhecido: ${menuItem.itemId}")
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        setupSearchView(searchView)

        return super.onCreateOptionsMenu(menu)
    }

    private fun setupSearchView(searchView: SearchView) {
        searchView.queryHint = getString(R.string.search_player)

        val searchEditText =
            searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.white))

        val closeIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        closeIcon.setColorFilter(
            ContextCompat.getColor(this, R.color.white),
            PorterDuff.Mode.SRC_IN
        )
    }

    override fun updateActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun showFragment(fragment: Fragment) {
        if (currentFragment !== fragment) {
            replaceFragment(fragment)
            currentFragment = fragment
        }
    }

    private fun replaceFragment(
        fragment: Fragment,
        addToBackStack: Boolean = false,
        tag: String? = null
    ) {
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
        supportActionBar?.title = getString(R.string.toolbar_title_register)
    }

    override fun goToUpdatePlayer(player: PlayerEntity?) {
        val bundle = Bundle().apply {
            putParcelable("KEY_PLAYER", player)
        }
        val fragment = RegisterPlayerFragment()
        fragment.arguments = bundle
        replaceFragment(fragment, addToBackStack = true, "UpdatePlayerTag")
        binding?.drawTeamBottomNav?.visibility = View.GONE
        supportActionBar?.title = getString(R.string.toolbar_title_edit)
    }

    override fun goToResult(
        players: List<PlayerEntity>,
        qtdPlayer: Int,
        pos: Boolean,
        lvl: Boolean
    ) {
        val bundle = Bundle().apply {
            putParcelableArrayList("KEY_PLAYERS", ArrayList(players))
            putInt("KEY_QTD", qtdPlayer)
            putBoolean("KEY_POS", pos)
            putBoolean("KEY_LVL", lvl)
        }
        val fragment = ResultFragment()
        fragment.arguments = bundle
        replaceFragment(fragment, addToBackStack = true)
        binding?.drawTeamBottomNav?.visibility = View.GONE
        supportActionBar?.title = getString(R.string.toolbar_title_result)
    }

    override fun goToEditResult(
        teams: MutableList<TeamEntity>,
        pos: Boolean
    ) {
        val bundle = Bundle().apply {
            putParcelableArrayList("KEY_TEAMS", ArrayList(teams))
            putBoolean("KEY_POS", pos)
        }
        val fragment = EditResultFragment()
        fragment.arguments = bundle
        replaceFragment(fragment, addToBackStack = true)
        binding?.drawTeamBottomNav?.visibility = View.GONE
        supportActionBar?.title = getString(R.string.edit_teams)
    }

    /*override fun goToGroup(groupId: Long) {
       *//* val fragment = GroupWithPlayersFragment()
        replaceFragment(fragment, addToBackStack = true)
        binding?.drawTeamBottomNav?.visibility = View.GONE*//*
    }*/

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}

