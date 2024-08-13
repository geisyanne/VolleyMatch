package co.geisyanne.volleymatch.presentation.drawTeams.result

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.data.local.entity.TeamEntity
import co.geisyanne.volleymatch.databinding.FragmentEditResultBinding
import java.util.Collections

class EditResultFragment : Fragment(R.layout.fragment_edit_result) {

    //private val viewModel: ResultViewModel by viewModel()
    private var binding: FragmentEditResultBinding? = null
    private var resultAdapter: ResultAdapter? = null


    private var teams: ArrayList<TeamEntity>? = null
    private var showPosition: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditResultBinding.bind(view)

        setupArguments()
        setupView()
    }

    private fun setupArguments() {
        arguments?.let { bundle ->
            teams = bundle.getParcelableArrayList("KEY_TEAMS")
            showPosition = bundle.getBoolean("KEY_POS")
        }
    }

    private fun setupView() {
        val teamsMutable = teams?.toMutableList() ?: mutableListOf()
        resultAdapter = ResultAdapter(requireContext(),teamsMutable,showPosition)

        binding?.resultEditRvTeams?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = resultAdapter
        }

        val helper = ItemTouchHelper(
            ItemTouchHelper(androidx.recyclerview.widget.ItemTouchHelper.UP
                    or androidx.recyclerview.widget.ItemTouchHelper.DOWN,
                androidx.recyclerview.widget.ItemTouchHelper.LEFT)
        )
        helper.attachToRecyclerView(binding?.resultEditRvTeams)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun editResult() {

    }


    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }


    inner class ItemTouchHelper(dragDirs: Int, swipeDirs: Int) : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(
        dragDirs, swipeDirs
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition

            resultAdapter?.teams?.let { Collections.swap(it, from, to) }
            resultAdapter?.notifyItemMoved(from, to)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
    }

}

