package co.geisyanne.meuapp.presentation.drawTeams.result

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.data.local.AppDatabase
import co.geisyanne.meuapp.data.local.repository.PlayerLocalDataSource
import co.geisyanne.meuapp.databinding.FragmentDrawBinding
import co.geisyanne.meuapp.databinding.FragmentResultBinding
import co.geisyanne.meuapp.domain.repository.PlayerRepository
import co.geisyanne.meuapp.presentation.common.util.viewModelFactory
import co.geisyanne.meuapp.presentation.drawTeams.draw.DrawViewModel

class ResultFragment : Fragment(R.layout.fragment_result) {

    private var binding: FragmentResultBinding? = null
    private lateinit var viewModel: ResultViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResultBinding.bind(view)

        viewModel = ViewModelProvider(this)[ResultViewModel::class.java]




    }






    // HIDE THE SEARCH MENU
    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.menu_search)
        searchItem?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

}