package co.geisyanne.meuapp.drawTeams.players

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.common.model.Player
import co.geisyanne.meuapp.databinding.FragmentPlayerListBinding
import co.geisyanne.meuapp.drawTeams.home.FragmentAttachListener


class PlayersFragment : Fragment(R.layout.fragment_player_list) {

    private var binding: FragmentPlayerListBinding? = null
    private var fragmentAttachListener: FragmentAttachListener? = null
    private val adapter = PlayersAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlayerListBinding.bind(view)

        binding?.playerBtnRegister?.setOnClickListener {
            fragmentAttachListener?.goToRegisterPlayer()
        }

        binding?.playerRv?.layoutManager = LinearLayoutManager(requireContext())
        binding?.playerRv?.adapter = adapter


        val players = mutableListOf<Player>()
        players.add(
            Player(
                uuid = "1",
                name = "Jogador1",
                position = "Levantador",
                level = 3,
                group = null
            )
        )
        players.add(
            Player(
                uuid = "2",
                name = "Jogador2",
                position = "Ponta",
                level = 5,
                group = null
            )
        )


        adapter.items = players
        adapter.notifyDataSetChanged()



    }

    // CHECK: IF THE ACTIVITY IMPLEMENTS AN INTERFACE
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentAttachListener) {
            fragmentAttachListener = context
        }
    }

  // TODO: COLOCAR ON DESTROY

}