package co.geisyanne.meuapp.drawTeams.groups.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.common.model.Group
import co.geisyanne.meuapp.common.model.Player
import co.geisyanne.meuapp.databinding.FragmentGroupListBinding
import co.geisyanne.meuapp.drawTeams.groups.GroupsAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tsuryo.swipeablerv.SwipeLeftRightCallback

class GroupListFragment : Fragment(R.layout.fragment_group_list) {

    private var binding: FragmentGroupListBinding? = null
    private val adapter = GroupsAdapter()
    private var groups = mutableListOf<Group>()
    private var actionMode: ActionMode? = null

    @SuppressLint("NotifyDataSetChanged")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGroupListBinding.bind(view)

        binding?.run {
            groupBtnCreate.setOnClickListener {
                showCreateGroupDialog()
            }

            groupRv.layoutManager = LinearLayoutManager(requireContext())
            groupRv.adapter = adapter

            groupRv.setListener(object : SwipeLeftRightCallback.Listener {
                override fun onSwipedLeft(position: Int) {
                    removeItem(position)
                }

                override fun onSwipedRight(position: Int) {
                }
            })
        }

        fakeGroups()
        adapter.apply {
            items = groups
            notifyDataSetChanged()

            onItemClick = { enabledActionMode(it) } // TOOLBAR WITH ACTION
            onItemLongClick = { enabledActionMode(it) }
        }

    }

    // ACTIVATE ACTION MODE
    private fun enabledActionMode(position: Int) {
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
                        adapter.deleteGroups()
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
                            .forEach { it.selected = false}
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
            actionMode?.finish() // DISABLE TOOLBAR ACTION
        } else {
            actionMode?.title = "$size"
            actionMode?.invalidate()
        }

    }

    private fun removeItem(position: Int) {
        activity?.let {
            val builder = MaterialAlertDialogBuilder(it)

            builder.setMessage(R.string.confirm_group_deletion)
                .setPositiveButton(R.string.yes) { _, _ ->
                    groups.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
                .setNeutralButton(R.string.no) { dialog, _ ->
                    adapter.notifyItemChanged(position)
                    dialog.cancel()
                }
            builder.create().show()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    private fun showCreateGroupDialog() {
        activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            val inflater = requireActivity().layoutInflater

            val dialogView = inflater.inflate(R.layout.dialog_custom, null)
            val editText = dialogView.findViewById<EditText>(R.id.group_register_edit_name)

            builder.setView(dialogView)
                .setPositiveButton(R.string.create) { _, _ ->

                    val newGroupName = editText.text.toString()
                    Log.i("Grupo", newGroupName)

                }
                .setNeutralButton(R.string.cancel) { dialog, _ ->

                    Log.e("teste", "cancelou")
                    dialog.cancel()
                }
            builder.create().show()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun fakeGroups() {
        val players1 = mutableListOf<Player>()
        players1.add(
            Player(
                name = "Jogador1",
                position = "Central",
                level = 1,
                group = null
            )
        )
        val players2 = mutableListOf<Player>()
        players2.add(
            Player(
                name = "Jogador2",
                position = "Levantador",
                level = 3,
                group = null
            )
        )
        players2.add(
            Player(
                name = "Jogador3",
                position = "Ponta",
                level = 5,
                group = null
            )
        )

        groups.add(
            Group(
                name = "Univolei",
                players = players1
            )
        )
        groups.add(
            Group(
                name = "UVP",
                players = players2
            )
        )
        groups.add(
            Group(
                name = "CVP",
                players = emptyList()
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        // DESTROY ACTION MODE
        if(actionMode != null)
            actionMode?.finish()
    }


}