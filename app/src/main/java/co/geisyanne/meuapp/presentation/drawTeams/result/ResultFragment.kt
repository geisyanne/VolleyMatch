package co.geisyanne.meuapp.presentation.drawTeams.result

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.databinding.FragmentResultBinding
import co.geisyanne.meuapp.domain.model.Team

class ResultFragment : Fragment(R.layout.fragment_result) {

    private var binding: FragmentResultBinding? = null
    private lateinit var viewModel: ResultViewModel

    private lateinit var adapter: ResultAdapter
    private var teams: MutableList<Team> = mutableListOf()
    private var showPosition: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResultBinding.bind(view)

        viewModel = ViewModelProvider(this)[ResultViewModel::class.java]


        arguments?.let { bundle ->
            val players: ArrayList<PlayerEntity>? = bundle.getParcelableArrayList("KEY_PLAYERS")
            val qtdPlayer: Int = bundle.getInt("KEY_QTD")
            val pos: Boolean = bundle.getBoolean("KEY_POS")
            val lvl: Boolean = bundle.getBoolean("KEY_LVL")

            showPosition = pos
            teams = players?.let { viewModel.drawTeams(it, qtdPlayer, pos, lvl) } ?: mutableListOf()
        }

        binding?.resultRvTeams?.layoutManager = LinearLayoutManager(requireContext())
        adapter = ResultAdapter(requireContext(), teams, showPosition)
        binding?.resultRvTeams?.adapter = adapter

    }


    // HIDE THE SEARCH MENU
    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.menu_search)
        searchItem?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

}