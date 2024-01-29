package co.geisyanne.meuapp.presentation.drawTeams.player.register

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.data.local.AppDatabase
import co.geisyanne.meuapp.data.local.repository.PlayerLocalDataSource
import co.geisyanne.meuapp.presentation.common.util.TxtWatcher
import co.geisyanne.meuapp.databinding.FragmentPlayerRegisterBinding
import co.geisyanne.meuapp.domain.repository.PlayerRepository
import co.geisyanne.meuapp.presentation.common.extension.hideKeyboard
import co.geisyanne.meuapp.presentation.common.util.viewModelFactory
import com.google.android.material.snackbar.Snackbar


class RegisterPlayerFragment : Fragment(R.layout.fragment_player_register) {

    private var binding: FragmentPlayerRegisterBinding? = null

    private lateinit var viewModel: RegisterPlayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlayerRegisterBinding.bind(view)


        val playerDao = AppDatabase.getInstance(requireContext()).playerDao
        val repository: PlayerRepository = PlayerLocalDataSource(playerDao)
        val factory = viewModelFactory { RegisterPlayerViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[RegisterPlayerViewModel::class.java]


        observeEvents()
        setListeners()

/*        binding?.playerRegisterEditName?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) hideKeyboard()
        }*/

        binding?.playerRegisterEditName?.addTextChangedListener(TxtWatcher {
            binding?.playerRegisterBtnSave?.isEnabled = it.isNotEmpty()
        })

        // SET ARRAY POSITIONS
        val positions = resources.getStringArray(R.array.positions)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, positions)

        binding?.playerRegisterTxtDropdownPositions?.apply {
            setAdapter(arrayAdapter)
            setText(arrayAdapter.getItem(0), false)
            setOnClickListener {
                showDropDown()
            }
        }

        // SET ARRAY GROUPS
        // binding?.containerDropdownGroups?.visibility = View.GONE

        val btnSave = binding?.playerRegisterBtnSave
        btnSave?.setOnClickListener {

            val namePlayer = binding?.playerRegisterEditName?.text.toString()
            val positionPlayer =
                arrayAdapter.getPosition(binding?.playerRegisterTxtDropdownPositions?.text.toString())
            val levelPlayer = binding?.playerRegisterRatingbar?.rating?.toInt()
            val groupPlayer = null

            viewModel.addPlayer(namePlayer, positionPlayer, levelPlayer, groupPlayer)


            Log.i("Teste", "$namePlayer - $positionPlayer - $levelPlayer ")
        }

    }


    private fun observeEvents() {
        viewModel.playerStateEventData.observe(viewLifecycleOwner) { playerState ->
            when (playerState) {
                is RegisterPlayerViewModel.PlayerState.Inserted -> {
                    clearFields()
                    hideKeyboard()
                }
            }
        }

        viewModel.messageEventData.observe(viewLifecycleOwner) { stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun clearFields() {
        binding?.playerRegisterEditName?.text?.clear()
        binding?.playerRegisterRatingbar?.rating = 0f
        // zerar positions
        // zerar groups
    }

    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity) {
            parentActivity.hideKeyboard()
        }
    }

    private fun setListeners() {

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.menu_search)
        searchItem?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    /*
            override fun showProgress(enabled: Boolean) {
                binding?.playerRegisterBtnSave?.showProgress(enabled)
            }

            override fun onRegisterFailure(message: String) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }

            override fun onRegisterSuccess() {
                // FECHAR TELA DE REGISTRO
                // VOLTAR PARA TELA DE PLAYERS LIST
                Toast.makeText(requireContext(), "Save Player", Toast.LENGTH_LONG).show()
            }*/


    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}