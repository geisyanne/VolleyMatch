package co.geisyanne.meuapp.drawTeams.group

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
import co.geisyanne.meuapp.common.model.Group


class GroupAdapter : RecyclerView.Adapter<GroupAdapter.GroupsViewHolder>() {

    var items: MutableList<Group> = mutableListOf()
    val selectedItems = SparseBooleanArray()
    private var currentSelectedPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsViewHolder {
        return GroupsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_group, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: GroupsViewHolder, position: Int) {
        holder.bind(items[position])

        // ACTIVATE CLICKS ON ITEMS
        holder.itemView.setOnClickListener {
            if (selectedItems.isNotEmpty()) { // IF AN ITEM ALREADY IS SELECTED
                onItemClick?.invoke(position)
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
            items[position].selected = false
        } else {
            selectedItems.put(position, true) // INSERTED/MARKED IN SPARSEBOOLEANARRAY
            items[position].selected = true
        }
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteGroups() {
        val selectGroups = items.filter { it.selected }

        if (selectGroups.isNotEmpty()) {
            items.removeAll(selectGroups)
            notifyDataSetChanged()
            currentSelectedPos = -1
        }
    }

    var onItemClick: ((Int) -> Unit)? = null
    var onItemLongClick: ((Int) -> Unit)? = null

    inner class GroupsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(group: Group) {
            itemView.findViewById<TextView>(R.id.item_group_txt_name).text = group.name

            val groupSize = group.players?.size
            val groupNumPlayerTxt = itemView.findViewById<TextView>(R.id.item_group_txt_players)

            groupNumPlayerTxt.text = when (groupSize) {
                0 -> itemView.context.resources.getString(R.string.no_player)
                1 -> itemView.context.resources.getString(R.string.qtd_player, groupSize)
                else -> itemView.context.resources.getString(R.string.qtd_players, groupSize)
            }

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