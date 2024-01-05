package co.geisyanne.meuapp.drawTeams.draw

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.FragmentDrawTeamsBinding
import co.geisyanne.meuapp.databinding.FragmentGroupListBinding

class DrawFragment : Fragment(R.layout.fragment_draw_teams) {

    private var binding: FragmentDrawTeamsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDrawTeamsBinding.bind(view)


    }


    // HIDE THE SEARCH MENU
    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchItem = menu.findItem(R.id.menu_search)
        searchItem?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

}