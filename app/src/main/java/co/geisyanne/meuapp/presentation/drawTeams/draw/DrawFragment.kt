package co.geisyanne.meuapp.presentation.drawTeams.draw

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.data.local.AppDatabase
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.data.local.repository.PlayerLocalDataSource
import co.geisyanne.meuapp.databinding.FragmentDrawBinding
import co.geisyanne.meuapp.domain.repository.PlayerRepository
import co.geisyanne.meuapp.presentation.common.util.viewModelFactory
import co.geisyanne.meuapp.presentation.drawTeams.home.FragmentAttachListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.snackbar.Snackbar


class DrawFragment : Fragment(R.layout.fragment_draw) {

    private lateinit var viewModel: DrawViewModel
    private var binding: FragmentDrawBinding? = null
    private var fragmentAttachListener: FragmentAttachListener? = null

    private lateinit var adapter: DrawAdapter
    private var selectedQtdAdapter: Int = 2
    private var selectedPlayer: List<PlayerEntity> = listOf()

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
                    val alert =
                        Snackbar.make(requireView(), R.string.alert_selection, Snackbar.LENGTH_LONG)
                    alert.setBackgroundTint(resources.getColor(R.color.blue_dark))
                    alert.show()
                } else {
                    showDrawParamsDialog()
                }
            }
        }
    }

    private fun observeViewModelEvents() {
        binding?.drawSelectProgress?.visibility = View.VISIBLE

        viewModel.allPlayersEvent.observe(viewLifecycleOwner) { allPlayers ->
            binding?.drawSelectProgress?.visibility = View.GONE

            adapter = DrawAdapter(requireContext(), allPlayers)
            binding?.drawRv?.adapter = adapter
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
                val paramList = selectedPlayer
                val paramQtd = selectedQtdAdapter
                val switchPosition =
                    dialogView.findViewById<MaterialSwitch>(R.id.draw_switch_position)?.isChecked
                        ?: false
                val switchLevel =
                    dialogView.findViewById<MaterialSwitch>(R.id.draw_switch_lvl)?.isChecked
                        ?: false

                fragmentAttachListener?.goToResult(paramList, paramQtd, switchPosition, switchLevel)
                Log.i("DrawParamsDialog", "paramList: $paramList, paramQtd: $paramQtd, switchPosition: $switchPosition, switchLevel: $switchLevel")

                dialog.dismiss()
            }
        } ?: throw IllegalStateException("Activity cannot be null")
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
        val searchItem = menu.findItem(R.id.menu_search)
        searchItem?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.drawTeam_bottom_nav)?.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}