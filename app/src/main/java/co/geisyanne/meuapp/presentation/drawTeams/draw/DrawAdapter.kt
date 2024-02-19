package co.geisyanne.meuapp.presentation.drawTeams.draw

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.data.local.entity.PlayerEntity


class DrawAdapter(
    private val context: Context,
    private var players: List<PlayerEntity>
) : RecyclerView.Adapter<DrawAdapter.ViewHolder>() {


    private val selectedItems = mutableListOf<PlayerEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_player_select, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = players.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(players[position])

    }

    fun getSelectedItems() : List<PlayerEntity> {
        return selectedItems.toList()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val checkBox: CheckBox = itemView.findViewById(R.id.item_rv_checkbox_player)

        init {
            checkBox.setOnClickListener {
                val selectedItem = players[adapterPosition]
                if (checkBox.isChecked) {
                    selectedItems.add(selectedItem)
                } else {
                    selectedItems.remove(selectedItem)
                }
            }
        }

        fun bind(player: PlayerEntity) {

            val positions = context.resources.getStringArray(R.array.positionsPlayer)
            val positionPlayer = positions[player.positionPlayer ?: 0]

            itemView.findViewById<TextView>(R.id.item_rv_txt_name).text = player.name
            itemView.findViewById<TextView>(R.id.item_rv_txt_pos).text = positionPlayer
            itemView.findViewById<RatingBar>(R.id.item_rv_ratingBar).rating =
                player.level?.toFloat() ?: 0f

        }
    }
}
