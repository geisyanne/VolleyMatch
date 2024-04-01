package co.geisyanne.volleymatch.presentation.drawTeams.draw

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.data.local.AppDatabase
import co.geisyanne.volleymatch.data.local.entity.PlayerEntity
import co.geisyanne.volleymatch.data.local.repository.PlayerLocalDataSource
import co.geisyanne.volleymatch.databinding.FragmentDrawBinding
import co.geisyanne.volleymatch.domain.repository.PlayerRepository
import co.geisyanne.volleymatch.presentation.common.util.viewModelFactory
import co.geisyanne.volleymatch.presentation.drawTeams.home.FragmentAttachListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.snackbar.Snackbar


class DrawFragment : Fragment(R.layout.fragment_draw) {

    private var viewModel: DrawViewModel? = null
    private var binding: FragmentDrawBinding? = null
    private var fragmentAttachListener: FragmentAttachListener? = null

    private lateinit var adapter: DrawAdapter
    private var selectedQtdAdapter: Int = 2
    private var selectedPlayer: List<PlayerEntity> = emptyList()

    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDrawBinding.bind(view)

        setupViewModel()
        setupUI()
        observeViewModelEvents()
    }

    private fun setupViewModel() {
        val playerDao = AppDatabase.getInstance(requireContext()).playerDao
        val repository: PlayerRepository = PlayerLocalDataSource(playerDao)
        val factory = viewModelFactory { DrawViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[DrawViewModel::class.java]
    }

    private fun setupUI() {
        binding?.run {
            drawRv.layoutManager = LinearLayoutManager(requireContext())

            drawBtnNext.setOnClickListener {
                selectedPlayer = adapter.getSelectedItems().toList()
                if (selectedPlayer.isEmpty()) {
                    showSnackbar(R.string.alert_selection)
                } else {
                    showDrawParamsDialog()
                }
            }
        }
    }

    private fun showSnackbar(messageResId: Int) {
        binding?.let {
            Snackbar.make(it.root, messageResId, 300)
                .setBackgroundTint(resources.getColor(R.color.blue_dark))
                .show()
        }
    }

    private fun showDrawParamsDialog() {
        activity?.let { activity ->
            val dialogView =
                LayoutInflater.from(activity).inflate(R.layout.dialog_draw_params, null)

            // SET DROPDOWN OF QTD OF PLAYERS
            val dropdown = dialogView.findViewById<AutoCompleteTextView>(R.id.draw_dropdown_num)
            val qtdPlayers = resources.getStringArray(R.array.numbers)
            val qtdPlayersAdapter = ArrayAdapter(activity, R.layout.item_dropdown, qtdPlayers)
            dropdown.apply {
                setAdapter(qtdPlayersAdapter)
                setText(qtdPlayersAdapter.getItem(0), false)
                setOnClickListener { showDropDown() }
                setOnItemClickListener { adapter, _, position, _ ->
                    selectedQtdAdapter = adapter.getItemIdAtPosition(position).toInt() + 2
                }
            }

            // SET DIALOG
            val dialog = MaterialAlertDialogBuilder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .create()
                .apply { show() }

            // SET CLICK BTN FOR RESULT
            dialogView.findViewById<Button>(R.id.draw_btn_go_result)?.setOnClickListener {
                val playerList = selectedPlayer
                val qtdPlayer = selectedQtdAdapter
                val switchPosition =
                    dialogView.findViewById<MaterialSwitch>(R.id.draw_switch_position)?.isChecked
                        ?: false
                val switchLevel =
                    dialogView.findViewById<MaterialSwitch>(R.id.draw_switch_lvl)?.isChecked
                        ?: false

                fragmentAttachListener?.goToResult(playerList, qtdPlayer, switchPosition, switchLevel)

                dialog.dismiss()
            }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun observeViewModelEvents() {
        binding?.drawSelectProgress?.visibility = View.VISIBLE
        viewModel?.allPlayersEvent?.observe(viewLifecycleOwner) { allPlayers ->
            binding?.drawSelectProgress?.visibility = View.GONE
            adapter = DrawAdapter(requireContext(), allPlayers)
            binding?.drawRv?.adapter = adapter
            adapter.onItemSelect = { enableActionMode() }

            binding?.drawTxtEmpty?.visibility = if (allPlayers.isEmpty()) View.VISIBLE else View.GONE

        }
    }

    // ACTIVATE ACTION MODE
    private fun enableActionMode() {
        if (actionMode == null && activity is AppCompatActivity) {
            actionMode = (activity as AppCompatActivity).startSupportActionMode(object :
                ActionMode.Callback {
                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    mode?.menuInflater?.inflate(R.menu.menu_draw, menu)
                    return true
                }

                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                    val menuSelectAll = R.id.action_selected_all
                    val menuClearAll = R.id.action_clear_all

                    when (item?.itemId) {
                        menuSelectAll -> {
                            adapter.selectAllItems()
                            toggleMenuItemVisibility(mode, menuSelectAll, false)
                            toggleMenuItemVisibility(mode, menuClearAll, true)
                            setTitleActionMode(adapter.listSelectedPlayers.size)
                            return true
                        }

                        menuClearAll -> {
                            adapter.deselectAllItems()
                            toggleMenuItemVisibility(mode, menuClearAll, false)
                            toggleMenuItemVisibility(mode, menuSelectAll, true)
                            setTitleActionMode(adapter.listSelectedPlayers.size)
                            return true
                        }
                    }
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode?) {
                    adapter.deselectAllItems()
                    actionMode = null
                }
            })
        }

        val size = adapter.listSelectedPlayers.size
        setTitleActionMode(size)
    }

    private fun setTitleActionMode(size: Int) {
        actionMode?.apply {
            if (size == 0) {
                finish() // DISABLE TOOLBAR ACTION
            } else {
                title = size.toString()
                invalidate()
            }
        }
    }

    private fun toggleMenuItemVisibility(mode: ActionMode?, itemId: Int, isVisible: Boolean) {
        mode?.menu?.findItem(itemId)?.isVisible = isVisible
    }

    // CHECK: IF THE ACTIVITY IMPLEMENTS AN INTERFACE
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentAttachListener) {
            fragmentAttachListener = context
        }
    }

    // HIDE THE SEARCH MENU
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_search)?.isVisible = false
    }

    override fun onResume() {
        super.onResume()
        selectedQtdAdapter = 2
        activity?.findViewById<View>(R.id.drawTeam_bottom_nav)?.visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.toolbar_title_drawTeams)
    }

    override fun onPause() {
        super.onPause()

        if (actionMode != null)
            actionMode?.finish()
    }

    override fun onDestroy() {
        binding = null
        viewModel = null

        if (actionMode != null)
            actionMode?.finish()

        super.onDestroy()
    }

}