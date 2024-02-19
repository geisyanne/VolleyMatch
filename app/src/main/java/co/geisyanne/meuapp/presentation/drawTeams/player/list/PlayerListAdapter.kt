package co.geisyanne.meuapp.presentation.drawTeams.player.list

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
import co.geisyanne.meuapp.data.local.entity.PlayerEntity

class PlayerListAdapter(
    var players: List<PlayerEntity>
) : RecyclerView.Adapter<PlayerListAdapter.PlayerListViewHolder>() {

    val selectedItems = SparseBooleanArray()
    private var currentSelectedPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerListViewHolder {
        return PlayerListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_player, parent, false)
        )
    }

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: PlayerListViewHolder, position: Int) {
        holder.bind(players[position])

        // ACTIVATE CLICKS ON ITEMS
        holder.itemView.setOnClickListener {

            if (selectedItems.isNotEmpty()) { // IF AN ITEM ALREADY IS SELECTED
                onItemClickSelect?.invoke(position)
            } else {
                onItemClickUpdate?.invoke(players[position]) // TO EDIT PLAYER
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
            players[position].selected = false
        } else {
            selectedItems.put(position, true) // INSERTED/MARKED IN SPARSEBOOLEANARRAY
            players[position].selected = true
        }
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getSelectedPlayers(): List<PlayerEntity>? {
        val selectedPlayers =
            players.filter { it.selected }  // CREATE NEW LIST WITH SELECTED PLAYERS
        currentSelectedPos = -1
        return selectedPlayers.takeIf { it.isNotEmpty() }
    }

    fun submitList(newList: List<PlayerEntity>) {
        players = newList
        notifyDataSetChanged()
    }

    var onItemClickUpdate: ((entity: PlayerEntity) -> Unit)? = null
    var onItemClickSelect: ((Int) -> Unit)? = null
    var onItemLongClick: ((Int) -> Unit)? = null

    inner class PlayerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(player: PlayerEntity) {
            itemView.findViewById<TextView>(R.id.txt_name).text = player.name

            // CHANGE COLOR FOR SELECTED ITEM
            if (player.selected) {
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
