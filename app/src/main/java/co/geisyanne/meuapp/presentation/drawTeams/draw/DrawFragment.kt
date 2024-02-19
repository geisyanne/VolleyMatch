package co.geisyanne.meuapp.presentation.drawTeams.draw

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.data.local.AppDatabase
import co.geisyanne.meuapp.data.local.repository.PlayerLocalDataSource
import co.geisyanne.meuapp.databinding.FragmentDrawBinding
import co.geisyanne.meuapp.domain.repository.PlayerRepository
import co.geisyanne.meuapp.presentation.common.util.viewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.snackbar.Snackbar


class DrawFragment : Fragment(R.layout.fragment_draw) {

    private lateinit var viewModel: DrawViewModel
    private var binding: FragmentDrawBinding? = null

    private lateinit var adapter: DrawAdapter

    private var qtdPlayersAdapter: ArrayAdapter<String>? = null
    private var selectedQtd: Int? = null

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

            drawBtnNext.setOnClickListener { showDrawParamsDialog() }
        }
    }

    private fun observeViewModelEvents() {
        viewModel.allPlayersEvent.observe(viewLifecycleOwner) { allPlayers ->
            adapter = DrawAdapter(requireContext(), allPlayers)
            binding?.drawRv?.adapter = adapter
        }
    }

    private fun showDrawParamsDialog() {

        val activity = activity ?: throw IllegalStateException("Activity cannot be null")


        val builder = MaterialAlertDialogBuilder(activity)
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_draw_params, null)

        // SET ARRAY NUMBER OF PLAYERS
        val dropdown = dialogView.findViewById<AutoCompleteTextView>(R.id.draw_dropdown_num)
        val qtdPlayers = resources.getStringArray(R.array.numbers)
        qtdPlayersAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, qtdPlayers)

        dropdown.apply {
            setAdapter(qtdPlayersAdapter)
            setText(qtdPlayersAdapter?.getItem(0), false)
            setOnClickListener { showDropDown() }

            setOnItemClickListener { adapter, _, position, _ ->
                val selected = adapter.getItemIdAtPosition(position).toInt()
                selectedQtd = selected + 2
            }
        }

        // SET DIALOG
        builder.setView(dialogView)
            .setCancelable(true)
            .create()
            .show()

        val drawBtn = dialogView.findViewById<Button>(R.id.draw_btn_go_result)

        drawBtn.setOnClickListener {
            val playersSelected = adapter.getSelectedItems()

            if (playersSelected.isEmpty()) {
                val alert =
                    Snackbar.make(requireView(), R.string.alert_selection, Snackbar.LENGTH_LONG)
                alert.setBackgroundTint(resources.getColor(R.color.blue_dark))
                alert.show()
            }

        }
    }

    // HIDE THE SEARCH MENU
    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.menu_search)
        searchItem?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}