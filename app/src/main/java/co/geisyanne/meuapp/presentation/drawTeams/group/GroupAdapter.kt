package co.geisyanne.meuapp.presentation.drawTeams.group

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.util.isNotEmpty
import androidx.recyclerview.widget.RecyclerView
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.data.local.entity.GroupEntity


class GroupAdapter(
    var groups: List<GroupEntity>
) : RecyclerView.Adapter<GroupAdapter.GroupsViewHolder>() {

    val selectedItems = SparseBooleanArray()
    private var currentSelectedPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolder {
        return GroupsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_group, parent, false)
        )
    }

    override fun getItemCount() = groups.size

    override fun onBindViewHolder(holder: GroupsViewHolder, position: Int) {
        holder.bind(groups[position])

        // ACTIVATE CLICKS ON ITEMS
        holder.itemView.setOnClickListener {

            if (selectedItems.isNotEmpty()) { // IF AN ITEM ALREADY IS SELECTED
                onItemClickSelect?.invoke(position)
            } else {
                onItemClickUpdate?.invoke(groups[position]) // TO EDIT GROUP
            }
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(position)
            return@setOnLongClickListener true
        }

        if (currentSelectedPos == position) currentSelectedPos = -1

    }

    // TOGGLE SELECTION OF ITEMS ACCORDING TO CLICK
    fun toggleSelection(position: Int) {

        currentSelectedPos = position

        if (selectedItems[position, false]) {
            selectedItems.delete(position) // REMOVED/UNCHECKED FROM SPARSEBOOLEANARRAY
            groups[position].selected = false
        } else {
            selectedItems.put(position, true) // INSERTED/MARKED IN SPARSEBOOLEANARRAY
            groups[position].selected = true
        }
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getSelectedGroups(): List<GroupEntity>? {
        val selectedGroups =
            groups.filter { it.selected }  // CREATE NEW LIST WITH SELECTED PLAYERS
        currentSelectedPos = -1
        return selectedGroups.takeIf { it.isNotEmpty() }
    }

    fun submitList(newList: List<GroupEntity>) {
        groups = newList
        notifyDataSetChanged()
    }

    var onItemClickUpdate: ((entity: GroupEntity) -> Unit)? = null
    var onItemClickSelect: ((Int) -> Unit)? = null
    var onItemLongClick: ((Int) -> Unit)? = null

    inner class GroupsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(group: GroupEntity) {
            itemView.findViewById<TextView>(R.id.item_group_txt_name).text = group.name

            /*val groupSize =
            val qtdPlayerTxt = itemView.findViewById<TextView>(R.id.item_group_txt_players)

            qtdPlayerTxt.text = when (groupSize) {
                0 -> itemView.context.resources.getString(R.string.no_player)
                1 -> itemView.context.resources.getString(R.string.qtd_player, groupSize)
                else -> itemView.context.resources.getString(R.string.qtd_players, groupSize)
            }*/

            // CHANGE COLOR FOR SELECTED ITEM
            if (group.selected) {
                itemView.background = GradientDrawable().apply {
                    setColor(Color.rgb(232, 240, 253))
                }
            } else {
                itemView.background = GradientDrawable().apply {
                    setColor(Color.WHITE)
                }
            }
        }
    }

}