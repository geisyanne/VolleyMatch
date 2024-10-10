package co.geisyanne.volleymatch.presentation.drawTeams.home

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.data.local.entity.PlayerEntity
import co.geisyanne.volleymatch.databinding.ActivityDrawTeamsBinding
import co.geisyanne.volleymatch.presentation.drawTeams.draw.DrawFragment
import co.geisyanne.volleymatch.presentation.drawTeams.player.list.PlayerListFragment
import co.geisyanne.volleymatch.presentation.drawTeams.player.register.RegisterPlayerFragment
import co.geisyanne.volleymatch.presentation.drawTeams.result.ResultFragment
import co.geisyanne.volleymatch.presentation.main.MainActivity
import co.geisyanne.volleymatch.util.Ad
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeDrawTeamsActivity : AppCompatActivity(), FragmentAttachListener,
    PlayerListFragment.ActionBarTitleUpdater {

    private lateinit var binding: ActivityDrawTeamsBinding
    private lateinit var rootLayout: ConstraintLayout
    private val bannerAd: AdView by lazy { binding.bannerAd }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDrawTeamsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        replaceFragment(PlayerListFragment())

        bannerAd.visibility = View.GONE
        rootLayout = binding.containerDrawTeams

        CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            setupAd()
        }

    }

    override fun onPause() {
        super.onPause()
        bannerAd.pause()
    }

    override fun onResume() {
        super.onResume()
        bannerAd.resume()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.apply {
            title = getString(R.string.toolbar_title_players)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_home_filled_24)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        setupSearchView(searchView)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
    private fun setupAd() {
        Ad.initialize(this)
        Ad.loadBannerAd(bannerAd)
        observeKeyboardStateForAdVisibility()
    }


    private fun observeKeyboardStateForAdVisibility() {
        rootLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootLayout.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootLayout.rootView.height
            val keypadHeight = screenHeight - r.bottom
            bannerAd.visibility = if (keypadHeight > 100) View.GONE else View.VISIBLE
        }
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

    override fun goToRegisterPlayer() {
        navigateToFragment(
            fragment = RegisterPlayerFragment(),
            tag = "RegisterPlayerTag",
            titleResId = R.string.toolbar_title_register
        )
    }

    override fun goToUpdatePlayer(player: PlayerEntity?) {
        val bundle = Bundle().apply {
            putParcelable("KEY_PLAYER", player)
        }
        val fragment = RegisterPlayerFragment().apply { arguments = bundle }
        navigateToFragment(
            fragment = fragment,
            tag = "UpdatePlayerTag",
            titleResId = R.string.toolbar_title_edit
        )
    }

    override fun goToDrawTeams() {
        navigateToFragment(
            fragment = DrawFragment(),
            tag = null,
            titleResId = R.string.toolbar_title_drawTeams
        )
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
        val fragment = ResultFragment().apply { arguments = bundle }

        navigateToFragment(
            fragment = fragment,
            tag = null,
            titleResId = R.string.toolbar_title_result
        )
    }

    private fun navigateToFragment(
        fragment: Fragment,
        tag: String?,
        titleResId: Int
    ) {
        replaceFragment(fragment, true, tag, titleResId)
    }

    private fun replaceFragment(
        fragment: Fragment,
        addToBackStack: Boolean = false,
        tag: String? = null,
        titleResId: Int? = null
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true) // Permite reordenação das transações
        transaction.replace(R.id.drawTeam_container_fragment, fragment, tag)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()

        titleResId?.let {
            supportFragmentManager.executePendingTransactions() // Executa qualquer transação pendente imediatamente
            supportActionBar?.title = getString(titleResId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerAd.destroy()

    }

}

