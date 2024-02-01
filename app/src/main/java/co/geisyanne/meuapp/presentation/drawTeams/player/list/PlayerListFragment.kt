package co.geisyanne.meuapp.presentation.drawTeams.player.list

import android.content.ClipData.Item
import android.content.Context
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.data.local.AppDatabase
import co.geisyanne.meuapp.data.local.repository.PlayerLocalDataSource
import co.geisyanne.meuapp.databinding.FragmentPlayerListBinding
import co.geisyanne.meuapp.domain.repository.PlayerRepository
import co.geisyanne.meuapp.presentation.common.util.viewModelFactory
import co.geisyanne.meuapp.presentation.drawTeams.home.FragmentAttachListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tsuryo.swipeablerv.SwipeLeftRightCallback


class PlayerListFragment() : Fragment(R.layout.fragment_player_list) {

    private lateinit var viewModel: PlayerListViewModel
    private lateinit var adapter: PlayerListAdapter
    private var binding: FragmentPlayerListBinding? = null
    private var fragmentAttachListener: FragmentAttachListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlayerListBinding.bind(view)

        setupViewModel()
        setupUI()
        observeViewModelEvents()






    }

    private fun setupViewModel() {
        val playerDao = AppDatabase.getInstance(requireContext()).playerDao
        val repository: PlayerRepository = PlayerLocalDataSource(playerDao)
        val factory = viewModelFactory { PlayerListViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[PlayerListViewModel::class.java]
    }

    private fun setupUI() {
        binding?.run {
            playerRv.layoutManager = LinearLayoutManager(requireContext())
            playerBtnRegister.setOnClickListener { fragmentAttachListener?.goToRegisterPlayer() }

            playerRv.setListener(object  : SwipeLeftRightCallback.Listener {
                override fun onSwipedLeft(position: Int) {


                    deletePlayer(position)
                }

                override fun onSwipedRight(position: Int) {
                }

            })
        }


    }

    private fun deletePlayer(position: Int) {
        activity?.apply {
            MaterialAlertDialogBuilder(this)
                .setMessage(R.string.confirm_player_deletion)
                .setCancelable(false)
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.deletePlayer(position)
                    adapter.notifyItemRemoved(position)
                }
                .setNeutralButton(R.string.no) { dialog, _ ->
                    adapter.notifyItemChanged(position)
                    dialog.cancel()
                }
                .create()
                .show()
        } ?: throw IllegalStateException("Activity cannot be null")


    }

    private fun observeViewModelEvents() {
        viewModel.allPlayersEvent.observe(viewLifecycleOwner) { allPlayers ->
            adapter = PlayerListAdapter(allPlayers).apply {
                onItemClickUpdate = { player ->
                    fragmentAttachListener?.goToUpdatePlayer(player)
                }
            }
            binding?.playerRv?.adapter = adapter
        }
    }

    // CHECK: IF THE ACTIVITY IMPLEMENTS AN INTERFACE
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentAttachListener) {
            fragmentAttachListener = context
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}
