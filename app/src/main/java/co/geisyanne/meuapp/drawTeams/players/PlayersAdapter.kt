package co.geisyanne.meuapp.drawTeams.players

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.util.isNotEmpty
import androidx.recyclerview.widget.RecyclerView
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.common.model.Player


class PlayersAdapter : RecyclerView.Adapter<PlayersAdapter.PlayersViewHolder>() {

    var items: MutableList<Player> = mutableListOf()
    val selectedItems = SparseBooleanArray()
    private var currentSelectedPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {
        return PlayersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_player, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
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
    fun deletePlayers() {
        val selectedPlayers = items.filter { it.selected }  // CREATE NEW LIST WITH SELECTED PLAYERS

        if (selectedPlayers.isNotEmpty()) {
            items.removeAll(selectedPlayers)
            notifyDataSetChanged()
            currentSelectedPos = -1
        }
    }

    var onItemClick: ((Int) -> Unit)? = null
    var onItemLongClick: ((Int) -> Unit)? = null

    inner class PlayersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(player: Player) {
            itemView.findViewById<TextView>(R.id.item_player_txt_name).text = player.name

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