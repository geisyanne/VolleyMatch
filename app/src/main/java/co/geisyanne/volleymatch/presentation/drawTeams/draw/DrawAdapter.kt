package co.geisyanne.volleymatch.presentation.drawTeams.draw

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.data.local.entity.PlayerEntity


class DrawAdapter(
    private val context: Context,
    private var players: List<PlayerEntity>
) : RecyclerView.Adapter<DrawAdapter.ViewHolder>() {

    var listSelectedPlayers = mutableSetOf<PlayerEntity>()  // SET PARA NÃO SER DUPLICADO

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_player_select, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = players.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(players[position])
    }

    fun selectAllItems() {
        listSelectedPlayers.addAll(players)
        notifyDataSetChanged()
    }

    fun deselectAllItems() {
        listSelectedPlayers.clear()
        notifyDataSetChanged()
    }

    fun getSelectedItems(): Set<PlayerEntity> {
        return listSelectedPlayers
    }

    fun updateList(preSelectedPlayers: List<PlayerEntity>) {
        listSelectedPlayers.clear()
        listSelectedPlayers.addAll(preSelectedPlayers)
        notifyDataSetChanged()
    }

    var onItemSelect: ((Boolean) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val checkBox: CheckBox = itemView.findViewById(R.id.item_rv_checkbox_player)

        init {
            checkBox.setOnClickListener {
                val player = players[adapterPosition]
                if (checkBox.isChecked) {
                    listSelectedPlayers.add(player)
                } else {
                    listSelectedPlayers.remove(player)
                }
                onItemSelect?.invoke(listSelectedPlayers.isEmpty())
            }
        }

        fun bind(player: PlayerEntity) {

            val positions = context.resources.getStringArray(R.array.positionsPlayer)
            val positionPlayer = positions[player.positionPlayer]

            itemView.findViewById<TextView>(R.id.item_rv_txt_name).text = player.name
            itemView.findViewById<TextView>(R.id.item_rv_txt_pos).text = positionPlayer
            itemView.findViewById<RatingBar>(R.id.item_rv_ratingBar).rating = player.level.toFloat()

            checkBox.isChecked = listSelectedPlayers.contains(player)
        }
    }
}