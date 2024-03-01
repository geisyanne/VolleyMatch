package co.geisyanne.meuapp.presentation.drawTeams.player.list


import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.data.local.AppDatabase
import co.geisyanne.meuapp.data.local.repository.PlayerLocalDataSource
import co.geisyanne.meuapp.databinding.FragmentPlayerListBinding
import co.geisyanne.meuapp.domain.repository.PlayerRepository
import co.geisyanne.meuapp.presentation.common.util.viewModelFactory
import co.geisyanne.meuapp.presentation.drawTeams.home.FragmentAttachListener
import co.geisyanne.meuapp.presentation.main.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tsuryo.swipeablerv.SwipeLeftRightCallback


class PlayerListFragment : Fragment(R.layout.fragment_player_list) {

    private lateinit var viewModel: PlayerListViewModel
    private var binding: FragmentPlayerListBinding? = null
    private var fragmentAttachListener: FragmentAttachListener? = null

    private lateinit var adapter: PlayerListAdapter
    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlayerListBinding.bind(view)

        setupViewModel()
        setupUI()
        observeViewModelEvents()
    }

    private fun setupViewModel() {
        val playerDao = AppDatabase.getInstance(requireContext()).playerDao
        val repository: PlayerRepository = PlayerLocalDataSource(playerDao)
        val factory = viewModelFactory { PlayerListViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[PlayerListViewModel::class.java]
    }

    private fun setupUI() {
        binding?.run {
            playerRv.layoutManager = LinearLayoutManager(requireContext())
            playerBtnRegister.setOnClickListener { fragmentAttachListener?.goToRegisterPlayer() }
        }

        setupSwipeLeftRightDelete()
    }

    private fun setupSwipeLeftRightDelete() {
        binding?.playerRv?.setListener(object : SwipeLeftRightCallback.Listener {
            override fun onSwipedLeft(position: Int) {
                deleteConfirmationDialog(position)
            }

            override fun onSwipedRight(position: Int) {
            }
        })
    }

    private fun deleteConfirmationDialog(position: Int) {
        activity?.apply {
            MaterialAlertDialogBuilder(this)
                .setMessage(R.string.confirm_player_deletion)
                .setCancelable(false)
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.deletePlayer(position)
                    adapter.notifyItemRemoved(position)
                }
                .setNeutralButton(R.string.no) { dialog, _ ->
                    adapter.notifyItemChanged(position)
                    dialog.cancel()
                }
                .create()
                .show()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun observeViewModelEvents() {
        binding?.playerProgress?.visibility = View.VISIBLE

        viewModel.allPlayersEvent.observe(viewLifecycleOwner) { allPlayers ->
            binding?.playerProgress?.visibility = View.GONE

            adapter = PlayerListAdapter(allPlayers).apply {
                onItemClickUpdate = { player ->
                    fragmentAttachListener?.goToUpdatePlayer(player)
                }
            }
            binding?.playerRv?.adapter = adapter
            adapter.onItemClickSelect = { enableActionMode(it) }
            adapter.onItemLongClick = { enableActionMode(it) }
        }
    }

    // ACTIVATE ACTION MODE
    private fun enableActionMode(position: Int) {
        if (actionMode == null && activity is AppCompatActivity) {
            actionMode = (activity as AppCompatActivity).startSupportActionMode(object :
                ActionMode.Callback {
                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    mode?.menuInflater?.inflate(R.menu.menu_delete, menu)
                    return true
                }

                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                    if (item?.itemId == R.id.menu_action_delete) {
                        val selectedPlayers = adapter.getSelectedPlayers()
                        viewModel.deleteSelectedPlayers(selectedPlayers)
                        mode?.finish()
                        return true
                    }
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode?) {
                    adapter.apply {
                        selectedItems.clear()
                        players
                            .filter { it.selected }
                            .forEach { it.selected = false }
                        notifyDataSetChanged()
                    }
                    actionMode = null
                }
            })
        }

        // COUNT IN THE TOOLBAR
        adapter.toggleSelection(position)
        val size = adapter.selectedItems.size()
        if (size == 0) {
            actionMode?.finish()  // DISABLE TOOLBAR ACTION
        } else {
            actionMode?.title = "$size"
            actionMode?.invalidate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as SearchView

        // HIDE REGISTER BTN DURING SEARCH
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            binding?.playerBtnRegister?.visibility = if (hasFocus) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchDatabase(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchDatabase(newText)

                }
                return true
            }
        })
    }

    private fun searchDatabase(name: String) {
        val searchName = "%$name%"
        view?.let {
            viewLifecycleOwnerLiveData.observe(viewLifecycleOwner) { viewLifecycleOwner ->
                viewModel.searchPlayer(searchName).observe(viewLifecycleOwner) { list ->
                    list.let {
                        adapter.submitList(it)
                        binding?.playerTxtEmpty?.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }


    // CHECK: IF THE ACTIVITY IMPLEMENTS AN INTERFACE
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentAttachListener) {
            fragmentAttachListener = context
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.drawTeam_bottom_nav)?.visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_title_players)
    }

    interface ActionBarTitleUpdater {
        fun updateActionBarTitle(title: String)
    }

    override fun onDestroy() {
        binding = null

        // DESTROY ACTION MODE
        if (actionMode != null)
            actionMode?.finish()

        super.onDestroy()
    }

}


