package co.geisyanne.meuapp.drawTeams.register

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.FragmentPlayerRegisterBinding


class PlayerRegisterFragment : Fragment(R.layout.fragment_player_register) {


    private var binding: FragmentPlayerRegisterBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlayerRegisterBinding.bind(view)

        val positions = resources.getStringArray(R.array.positions)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, positions)

        binding?.playerRegisterTxtDropdownPositions?.apply {
            setAdapter(arrayAdapter)
            setText(arrayAdapter.getItem(0), false)
            setOnClickListener {
                showDropDown()
            }
        }


    }

}