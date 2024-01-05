package co.geisyanne.meuapp.drawTeams.home.view

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.FragmentInitialSortDrawTeamsBinding

class HomeDrawFragment: Fragment(R.layout.fragment_initial_sort_draw_teams) {

    private var binding: FragmentInitialSortDrawTeamsBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentInitialSortDrawTeamsBinding.bind(view)

    }

    // HIDE THE SEARCH MENU
    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.menu_search)
        searchItem?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

}