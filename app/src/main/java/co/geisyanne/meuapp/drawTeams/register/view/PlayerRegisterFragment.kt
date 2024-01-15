package co.geisyanne.meuapp.drawTeams.register.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.common.util.TxtWatcher
import co.geisyanne.meuapp.databinding.FragmentPlayerRegisterBinding
import co.geisyanne.meuapp.drawTeams.register.RegisterPlayer
import co.geisyanne.meuapp.drawTeams.register.data.RegisterRepository
import co.geisyanne.meuapp.drawTeams.register.presenter.PlayerRegisterPresenter


class PlayerRegisterFragment : Fragment(R.layout.fragment_player_register), RegisterPlayer.View {


    private var binding: FragmentPlayerRegisterBinding? = null
    override lateinit var presenter: RegisterPlayer.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlayerRegisterBinding.bind(view)

        // val repository = RegisterRepository()

        presenter = PlayerRegisterPresenter(this)

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
            val positionPlayer = arrayAdapter.getPosition(binding?.playerRegisterTxtDropdownPositions?.text.toString())
            val levelPlayer = binding?.playerRegisterRatingbar?.rating?.toInt()
            val groupPlayer = null

            presenter.createPlayer(namePlayer,positionPlayer.toString(), levelPlayer, groupPlayer)


/*            Handler(Looper.getMainLooper()).postDelayed({
                btnSave.showProgress(false)
            }, 2000)*/

            Log.i("Teste", "$namePlayer - $positionPlayer - $levelPlayer ")
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.menu_search)
        searchItem?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }


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
    }


    override fun onDestroy() {
        binding = null
        presenter.onDestroy()
        super.onDestroy()
    }

}