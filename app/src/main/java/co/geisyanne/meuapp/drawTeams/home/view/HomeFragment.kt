package co.geisyanne.meuapp.drawTeams.home.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.FragmentInitialSortDrawTeamsBinding

class HomeFragment: Fragment(R.layout.fragment_initial_sort_draw_teams) {

    private var binding: FragmentInitialSortDrawTeamsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentInitialSortDrawTeamsBinding.bind(view)


    }

}