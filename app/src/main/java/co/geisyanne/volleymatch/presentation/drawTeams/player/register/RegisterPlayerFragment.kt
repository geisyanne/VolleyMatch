package co.geisyanne.volleymatch.presentation.drawTeams.player.register

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.data.local.entity.PlayerEntity
import co.geisyanne.volleymatch.databinding.FragmentPlayerRegisterBinding
import co.geisyanne.volleymatch.presentation.common.extension.hideKeyboard
import co.geisyanne.volleymatch.presentation.common.util.TxtWatcher
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterPlayerFragment : Fragment(R.layout.fragment_player_register) {

    private val viewModel: RegisterPlayerViewModel by viewModel()
    private var binding: FragmentPlayerRegisterBinding? = null

    private var id: Long = 0
    private var positionsAdapter: ArrayAdapter<String>? = null
    private var selectedPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlayerRegisterBinding.bind(view)

        setupUI()
    }

    private fun setupUI() {
        configureForm()
        observeEvents()
        setListeners()
    }

    private fun configureForm() {
        when (tag) {
            "RegisterPlayerTag" -> registerPlayerForm()
            "UpdatePlayerTag" -> updatePlayerForm()
        }
    }

    private fun registerPlayerForm() {
        binding?.playerRegisterEditName?.addTextChangedListener(TxtWatcher {
            binding?.playerRegisterBtnSave?.isEnabled = it.isNotEmpty()
        })
        configureDropdown(0)
    }

    private fun updatePlayerForm() {
        val player = arguments?.getParcelable<PlayerEntity>("KEY_PLAYER") ?: return
        selectedPosition = player.positionPlayer
        id = player.playerId

        binding?.playerRegisterBtnSave?.apply {
            setText(getString(R.string.edit))
            isEnabled = true
        }

        binding?.playerRegisterEditName?.setText(player.name)
        configureDropdown(selectedPosition)
        binding?.playerRegisterRatingbar?.rating = player.level.toFloat()
    }

    private fun configureDropdown(pos: Int) {

        // SET ARRAY POSITIONS
        val positions = resources.getStringArray(R.array.positionsPlayer)
        positionsAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, positions)

        binding?.playerRegisterTxtDropdownPositions?.apply {
            setAdapter(positionsAdapter)
            setText(positionsAdapter?.getItem(pos), false)
            setOnClickListener { showDropDown() }
            setOnItemClickListener { adapter, _, position, _ ->
                selectedPosition = adapter.getItemIdAtPosition(position).toInt()
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hideKeyboard()
                }
            }

        }
    }

    private fun observeEvents() {
        viewModel.playerStateEventData.observe(viewLifecycleOwner) { playerState ->
            when (playerState) {
                is RegisterPlayerViewModel.PlayerState.Inserted,
                is RegisterPlayerViewModel.PlayerState.Updated -> {
                    binding?.playerRegisterBtnSave?.showProgress(false)
                    hideKeyboard()
                    activity?.supportFragmentManager?.popBackStack()
                }
            }
        }

        viewModel.messageEventData.observe(viewLifecycleOwner) { stringResId ->
            Snackbar.make(requireView(), stringResId, 500)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.blue_dark))
                .show()
        }
    }

    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity) {
            parentActivity.hideKeyboard()
        }
    }

    private fun setListeners() {
        val btnSave = binding?.playerRegisterBtnSave

        btnSave?.setOnClickListener {
            btnSave.showProgress(true)
            val namePlayer = binding?.playerRegisterEditName?.text.toString()
            val positionPlayer = selectedPosition
            val levelPlayer = binding?.playerRegisterRatingbar?.rating?.toInt() ?: 0
            viewModel.addOrUpdatePlayer(namePlayer, positionPlayer, levelPlayer, id)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // HIDE THE SEARCH MENU
    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        searchItem?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}

