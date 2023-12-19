package co.geisyanne.meuapp.drawTeams.players

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.common.model.Player


class PlayersAdapter : RecyclerView.Adapter<PlayersAdapter.PlayersViewHolder>() {

    var items: List<Player> = mutableListOf()
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
    }

    inner class PlayersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(player: Player) {
            itemView.findViewById<TextView>(R.id.item_player_txt_name).text = player.name

            val moreBtn = itemView.findViewById<ImageButton>(R.id.item_player_btn_more)

            moreBtn.setOnClickListener {
                Log.i("teste", "clicou")
            }

        }



    }

}