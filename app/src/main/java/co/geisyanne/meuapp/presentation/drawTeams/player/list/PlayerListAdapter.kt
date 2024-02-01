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
import co.geisyanne.meuapp.domain.model.Player


class PlayerListAdapter(
    private val players: List<PlayerEntity>
) : RecyclerView.Adapter<PlayerListAdapter.PlayerListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerListViewHolder {
        return PlayerListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_player, parent, false)
        )
    }

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: PlayerListViewHolder, position: Int) {
        holder.bind(players[position])
    }

    var onItemClickUpdate: ((entity: PlayerEntity) -> Unit)? = null

    inner class PlayerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(player: PlayerEntity) {
            itemView.findViewById<TextView>(R.id.item_player_txt_name).text = player.name

            itemView.setOnClickListener {
                onItemClickUpdate?.invoke(player)
            }

        }
    }

}