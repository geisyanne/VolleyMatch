package co.geisyanne.meuapp.drawTeams.players

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.FragmentPlayerListBinding

class PlayersFragment : Fragment(R.layout.fragment_player_list) {

    private var binding: FragmentPlayerListBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlayerListBinding.bind(view)







    }



}