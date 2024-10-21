package co.geisyanne.volleymatch.presentation.drawTeams.player.list


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
import androidx.recyclerview.widget.LinearLayoutManager
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.databinding.FragmentPlayerListBinding
import co.geisyanne.volleymatch.presentation.drawTeams.home.FragmentAttachListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerListFragment : Fragment(R.layout.fragment_player_list) {

    private val viewModel: PlayerListViewModel by viewModel()
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

        setupUI()
        observeViewModelEvents()
    }

    private fun setupUI() {
        binding?.run {
            playerRv.layoutManager = LinearLayoutManager(requireContext())
            playerBtnRegister.setOnClickListener { fragmentAttachListener?.goToRegisterPlayer() }
            playerBtnGoDraw.setOnClickListener { fragmentAttachListener?.goToDrawTeams() }
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

            binding?.playerTxtEmpty?.visibility = if (allPlayers.isEmpty()) View.VISIBLE else View.GONE
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
            actionMode?.finish()
        } else {
            actionMode?.title = "$size"
            actionMode?.invalidate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        if (searchView != null) {
            // HIDE REGISTER BTN DURING SEARCH
            searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
                binding?.playerBtnRegister?.visibility = if (hasFocus) View.GONE else View.VISIBLE
                binding?.playerBtnGoDraw?.visibility = if (hasFocus) View.GONE else View.VISIBLE
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { searchDatabase(it) }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { searchDatabase(it) }
                    return true
                }
            })
        } else {
            FirebaseCrashlytics.getInstance().log("SearchView não disponível no Fragment")
            FirebaseCrashlytics.getInstance()
                .recordException(IllegalStateException("SearchView não encontrado no Fragment"))
        }
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
        //activity?.findViewById<View>(R.id.drawTeam_bottom_nav)?.visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_title_players)
    }

    interface ActionBarTitleUpdater {
        fun updateActionBarTitle(title: String)
    }

    override fun onDestroy() {
        binding = null

        if (actionMode != null)
            actionMode?.finish()

        super.onDestroy()
    }

}


