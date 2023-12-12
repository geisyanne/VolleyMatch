package co.geisyanne.meuapp.drawTeams.groups

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.databinding.FragmentGroupListBinding

class GroupListFragment : Fragment(R.layout.fragment_group_list) {

    private var binding: FragmentGroupListBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGroupListBinding.bind(view)



    }


}