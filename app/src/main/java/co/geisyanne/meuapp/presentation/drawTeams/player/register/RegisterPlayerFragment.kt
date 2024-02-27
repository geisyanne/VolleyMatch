package co.geisyanne.meuapp.presentation.drawTeams.player.register

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.data.local.AppDatabase
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.data.local.repository.PlayerLocalDataSource
import co.geisyanne.meuapp.databinding.FragmentPlayerRegisterBinding
import co.geisyanne.meuapp.domain.repository.PlayerRepository
import co.geisyanne.meuapp.presentation.common.extension.hideKeyboard
import co.geisyanne.meuapp.presentation.common.util.TxtWatcher
import co.geisyanne.meuapp.presentation.common.util.viewModelFactory
import co.geisyanne.meuapp.presentation.drawTeams.home.HomeDrawTeamsActivity
import com.google.android.material.snackbar.Snackbar


class RegisterPlayerFragment : Fragment(R.layout.fragment_player_register) {

    private var viewModel: RegisterPlayerViewModel? = null
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

        setupViewModel()
        setupUI()

    }

    private fun setupViewModel() {
        val playerDao = AppDatabase.getInstance(requireContext()).playerDao
        val repository: PlayerRepository = PlayerLocalDataSource(playerDao)
        val factory = viewModelFactory { RegisterPlayerViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[RegisterPlayerViewModel::class.java]
    }

    private fun setupUI() {
        configureDropdown()
        configureEditText()
        observeEvents()
        setListeners()
    }

    private fun configureDropdown() {

        // SET ARRAY POSITIONS
        val positions = resources.getStringArray(R.array.positionsPlayer)
        positionsAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, positions)

        binding?.playerRegisterTxtDropdownPositions?.apply {
            setAdapter(positionsAdapter)
            setText(positionsAdapter?.getItem(0), false)
            setOnClickListener {
                showDropDown()
            }
            binding?.playerRegisterTxtDropdownPositions?.setOnItemClickListener { adapter, _, position, _ ->
                selectedPosition = adapter.getItemIdAtPosition(position).toInt()
            }

            binding?.playerRegisterTxtDropdownPositions?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hideKeyboard()
                }
            }

        }
    }

    @Suppress("DEPRECATION")
    private fun configureEditText() {
        when (tag) {
            "RegisterPlayerTag" -> {
                binding?.playerRegisterEditName?.addTextChangedListener(TxtWatcher {
                    binding?.playerRegisterBtnSave?.isEnabled = it.isNotEmpty()
                })
            }

            "UpdatePlayerTag" -> {
                arguments?.getParcelable<PlayerEntity>("KEY_PLAYER")?.let { player ->
                    id = player.playerId
                    val posPlayer = player.positionPlayer

                    binding?.playerRegisterBtnSave?.apply {
                        setText(getString(R.string.edit))
                        isEnabled = true
                    }

                    binding?.run {
                        playerRegisterEditName.setText(player.name)
                        playerRegisterTxtDropdownPositions.setText(
                            positionsAdapter?.getItem(
                                posPlayer
                            ), false
                        )
                        playerRegisterRatingbar.rating = player.level.toFloat()
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        viewModel?.playerStateEventData?.observe(viewLifecycleOwner) { playerState ->
            when (playerState) {
                is RegisterPlayerViewModel.PlayerState.Inserted,
                is RegisterPlayerViewModel.PlayerState.Updated -> {
                    binding?.playerRegisterBtnSave?.showProgress(false)
                    hideKeyboard()
                    startDelayToClose()
                }
            }
        }

        viewModel?.messageEventData?.observe(viewLifecycleOwner) { stringResId ->
            val alert =
                Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG)
            alert.setBackgroundTint(resources.getColor(R.color.blue_dark))
            alert.show()
        }
    }

    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity) {
            parentActivity.hideKeyboard()
        }
    }

    private fun startDelayToClose() {
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            closeWithAnimation(requireView())
        }, 200.toLong())
    }

    private fun closeWithAnimation(view: View) {
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = 200
        fadeOut.fillAfter = true

        view.startAnimation(fadeOut)

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            activity?.supportFragmentManager?.popBackStack()
        }, fadeOut.duration)
    }

    private fun setListeners() {
        val btnSave = binding?.playerRegisterBtnSave

        btnSave?.setOnClickListener {
            btnSave.showProgress(true)
            val namePlayer = binding?.playerRegisterEditName?.text.toString()

            val positionPlayer = selectedPosition


            val levelPlayer = binding?.playerRegisterRatingbar?.rating?.toInt() ?: 0
            viewModel?.addOrUpdatePlayer(namePlayer, positionPlayer, levelPlayer, id)
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
        val searchItem = menu.findItem(R.id.menu_search)
        searchItem?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }


    override fun onDestroy() {
        binding = null
        viewModel = null
        super.onDestroy()
    }
}

