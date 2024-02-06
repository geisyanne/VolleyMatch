package co.geisyanne.meuapp.presentation.drawTeams.player.register

import android.os.Bundle
import android.os.Looper
import android.view.Menu
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
import com.google.android.material.snackbar.Snackbar


class RegisterPlayerFragment : Fragment(R.layout.fragment_player_register) {

    private var binding: FragmentPlayerRegisterBinding? = null
    private var viewModel: RegisterPlayerViewModel? = null

    private var id: Long = 0
    private val positionsAdapter: ArrayAdapter<String>? = null

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
        configureEditText()
        configureDropdown()
        observeEvents()
        setListeners()
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
                    binding?.run {
                        playerRegisterEditName.setText(player.name)
                        playerRegisterBtnSave.setText("Editar")
                    }
                    id = player.id
                }
            }
        }
    }

    private fun configureDropdown() {

        // SET ARRAY POSITIONS
        val positions = resources.getStringArray(R.array.positions)
        val positionsAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, positions)

        binding?.playerRegisterTxtDropdownPositions?.apply {
            setAdapter(positionsAdapter)
            setText(positionsAdapter.getItem(0), false)
            setOnClickListener {
                showDropDown()
            }
        }

        // SET ARRAY GROUPS TODO: to implement
        /*private fun setArrayGroups() {
        // binding?.containerDropdownGroups?.visibility = View.GONE
         }*/

    }

    private fun observeEvents() {
        viewModel?.playerStateEventData?.observe(viewLifecycleOwner) { playerState ->
            when (playerState) {
                is RegisterPlayerViewModel.PlayerState.Inserted,
                is RegisterPlayerViewModel.PlayerState.Updated -> {
                    clearFields()
                    hideKeyboard()
                    startDelayToClose()
                }

                is RegisterPlayerViewModel.PlayerState.Deleted -> {
                    // uq? TODO
                }
            }
        }

        viewModel?.messageEventData?.observe(viewLifecycleOwner) { stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun clearFields() {
        binding?.playerRegisterEditName?.text?.clear()
        binding?.playerRegisterRatingbar?.rating = 0f
        // zerar positions TODO
        // zerar groups
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
        }, 1000.toLong())
    }

    private fun closeWithAnimation(view: View) {
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = 500
        fadeOut.fillAfter = true

        view.startAnimation(fadeOut)

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            activity?.supportFragmentManager?.popBackStack()
        }, fadeOut.duration)
    }

    private fun setListeners() {
        val btnSave = binding?.playerRegisterBtnSave

        btnSave?.setOnClickListener {
            val namePlayer = binding?.playerRegisterEditName?.text.toString()
            val positionPlayer =
                positionsAdapter?.getPosition(binding?.playerRegisterTxtDropdownPositions?.text.toString())
            val levelPlayer = binding?.playerRegisterRatingbar?.rating?.toInt()
            val groupPlayer = null

            viewModel?.addOrUpdatePlayer(namePlayer, positionPlayer, levelPlayer, groupPlayer, id)
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
        viewModel = null
        super.onDestroy()
    }

}