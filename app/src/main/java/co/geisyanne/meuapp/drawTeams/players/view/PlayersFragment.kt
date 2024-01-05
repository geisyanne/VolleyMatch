package co.geisyanne.meuapp.drawTeams.players.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.common.model.Player
import co.geisyanne.meuapp.databinding.FragmentPlayerListBinding
import co.geisyanne.meuapp.drawTeams.home.FragmentAttachListener
import co.geisyanne.meuapp.drawTeams.players.PlayersAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tsuryo.swipeablerv.SwipeLeftRightCallback


class PlayersFragment : Fragment(R.layout.fragment_player_list) {

    private var binding: FragmentPlayerListBinding? = null
    private var fragmentAttachListener: FragmentAttachListener? = null
    private var adapter = PlayersAdapter()
    private var players = mutableListOf<Player>()
    private var actionMode: ActionMode? = null

    @SuppressLint("NotifyDataSetChanged")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPlayerListBinding.bind(view)

        binding?.run {

            playerBtnRegister.setOnClickListener {
                fragmentAttachListener?.goToRegisterPlayer()
            }

            playerRv.layoutManager = LinearLayoutManager(requireContext())
            playerRv.adapter = adapter

            playerRv.setListener(object : SwipeLeftRightCallback.Listener {
                override fun onSwipedLeft(position: Int) {
                    removeItem(position)
                }

                override fun onSwipedRight(position: Int) {
                }
            })
        }

        fakePlayers()
        adapter.apply {
            items = players
            notifyDataSetChanged()

            onItemClick = { enableActionMode(it) } // TOOLBAR WITH ACTION
            onItemLongClick = { enableActionMode(it) }
        }

    }

    private fun enableActionMode(position: Int) {
        if (actionMode == null && activity is AppCompatActivity) {
            actionMode = (activity as AppCompatActivity).startSupportActionMode(object :
                ActionMode.Callback {
                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    mode?.menuInflater?.inflate(R.menu.menu_delete, menu)
                    return true
                }

                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                    if (item?.itemId == R.id.menu_action_delete) {
                        adapter.deletePlayers()
                        mode?.finish()
                        return true
                    }
                    return false
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onDestroyActionMode(mode: ActionMode?) {
                    adapter.apply {
                        selectedItems.clear()
                        items
                            .filter { it.selected }
                            .forEach { it.selected = false }
                        notifyDataSetChanged()
                    }
                    actionMode = null
                }
            })
        }

        // COUNT IN THE TOOLBAR
        adapter.toggleSelection(position)
        val size = adapter.selectedItems.size()
        if (size == 0) {
            actionMode?.finish()  // DISABLE TOOLBAR ACTION
        } else {
            actionMode?.title = "$size"
            actionMode?.invalidate()
        }

    }

    private fun removeItem(position: Int) {
        activity?.let {
            val builder = MaterialAlertDialogBuilder(it)

            builder.setMessage(R.string.confirm_player_deletion)
                .setPositiveButton(R.string.yes) { _, _ ->
                    players.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    Log.i("teste", "excluiu")
                }
                .setNeutralButton(R.string.no) { dialog, _ ->
                    adapter.notifyItemChanged(position)
                    Log.e("teste", "cancelou")
                    dialog.cancel()
                }
            builder.create().show()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    // CHECK: IF THE ACTIVITY IMPLEMENTS AN INTERFACE
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentAttachListener) {
            fragmentAttachListener = context
        }
    }

    private fun fakePlayers() {
        for (i in 1..30) {
            val player = Player(
                uuid = i.toString(),
                name = "Jogador$i",
                position = "Posição$i",
                level = 2,
                group = null
            )
            players.add(player)
        }
    }

}