package co.geisyanne.volleymatch.presentation.drawTeams.group

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.data.local.AppDatabase
import co.geisyanne.volleymatch.data.local.repositoryImpl.GroupLocalDataSource
import co.geisyanne.volleymatch.databinding.FragmentGroupListBinding
import co.geisyanne.volleymatch.domain.repository.GroupRepository
import co.geisyanne.volleymatch.presentation.common.util.viewModelFactory
import co.geisyanne.volleymatch.presentation.drawTeams.home.FragmentAttachListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tsuryo.swipeablerv.SwipeLeftRightCallback

class GroupFragment : Fragment(R.layout.fragment_group_list) {

    private lateinit var viewModel: GroupViewModel
    private var binding: FragmentGroupListBinding? = null
    private var fragmentAttachListener: FragmentAttachListener? = null

    private var actionMode: ActionMode? = null
    private lateinit var adapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroupListBinding.bind(view)

        setupViewModel()
        setupUI()
        observeViewModelEvents()

    }

    private fun setupViewModel() {
        val groupDao = AppDatabase.getInstance(requireContext()).groupDao
        val repository: GroupRepository = GroupLocalDataSource(groupDao)
        val factory = viewModelFactory { GroupViewModel(repository) }
        viewModel = ViewModelProvider(this, factory)[GroupViewModel::class.java]
    }

    private fun setupUI() {
        binding?.run {
            groupRv.layoutManager = LinearLayoutManager(requireContext())
            groupBtnCreate.setOnClickListener { createOrUpdateGroupDialog(0) }
        }

        setupSwipeLeftRightDelete()
    }

    private fun createOrUpdateGroupDialog(id: Long) {
        activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            val inflater = requireActivity().layoutInflater

            val dialogView = inflater.inflate(R.layout.dialog_group, null)
            val editText = dialogView.findViewById<EditText>(R.id.group_register_edit_name)
            val string = getString(if (id > 0) R.string.edit else R.string.create)

            builder.setView(dialogView)
                .setPositiveButton(string) { _, _ ->
                    val name = editText.text.toString()
                    viewModel.addOrUpdateGroup(name, id)
                }
                .setNeutralButton(R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
            builder.create().show()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setupSwipeLeftRightDelete() {
        binding?.groupRv?.setListener(object : SwipeLeftRightCallback.Listener {
            override fun onSwipedLeft(position: Int) {
                deleteConfirmationDialog(position)
            }

            override fun onSwipedRight(position: Int) {
            }
        })
    }

    private fun deleteConfirmationDialog(position: Int) {
        activity?.apply {
            MaterialAlertDialogBuilder(this)
                .setMessage(R.string.confirm_player_deletion)
                .setCancelable(false)
                .setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.deleteGroup(position)
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
                        val selectedGroups = adapter.getSelectedGroups()
                        viewModel.deleteSelectedGroups(selectedGroups)
                        mode?.finish()
                        return true
                    }
                    return false
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onDestroyActionMode(mode: ActionMode?) {
                    adapter.apply {
                        selectedItems.clear()
                        groups
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

    private fun observeViewModelEvents() {
        viewModel.allGroupsEvent.observe(viewLifecycleOwner) { allGroups ->
            adapter = GroupAdapter(allGroups).apply {
                onItemClickUpdate = { group ->

                    //createOrUpdateGroupDialog(group.groupId)
                }
            }
            binding?.groupRv?.adapter = adapter
            adapter.onItemClickSelect = { enabledActionMode(it) }
            adapter.onItemLongClick = { enabledActionMode(it) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        // HIDE CREATE BTN DURING SEARCH
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            binding?.groupBtnCreate?.visibility = if (hasFocus) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchDatabase(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchDatabase(newText)

                }
                return true
            }
        })
    }

    private fun searchDatabase(name: String) {
        val searchName = "%$name%"
        view?.let {
            viewLifecycleOwnerLiveData.observe(viewLifecycleOwner) { viewLifecycleOwner ->
                viewModel.searchGroup(searchName).observe(viewLifecycleOwner) { list ->
                    list.let {
                        adapter.submitList(it)
                        binding?.groupTxtEmpty?.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        binding = null

        // DESTROY ACTION MODE
        if(actionMode != null)
            actionMode?.finish()

        super.onDestroy()
    }


}