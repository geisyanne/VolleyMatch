package co.geisyanne.volleymatch.presentation.drawTeams.result

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.data.local.entity.PlayerEntity
import co.geisyanne.volleymatch.data.local.entity.TeamEntity

class ResultAdapter(
    private val context: Context,
    var teams: MutableList<TeamEntity>,
    private val showPosition: Boolean,
) : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {


    private val currentNightMode = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        return ResultViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_result, parent, false)
        )
    }

    override fun getItemCount() = teams.size

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {

        val cardColors = if (currentNightMode) {
            intArrayOf(R.color.blue_medium_2, R.color.blue_medium)
        } else {
            intArrayOf(R.color.yellow_transparent, R.color.blue_transparent)
        }

        val color = ContextCompat.getColor(context, cardColors[position % cardColors.size])
        holder.itemView.findViewById<CardView>(R.id.item_cad_result).setCardBackgroundColor(color)

        holder.bind(teams[position])
    }

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val innerPlayersRv: RecyclerView =
            itemView.findViewById(R.id.item_result_rv_players)
        private val innerPlayersAdapter = InnerPlayersAdapter(itemView.context, showPosition)

        init {
            innerPlayersRv.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = innerPlayersAdapter
            }
        }

        fun bind(team: TeamEntity) {

            val numTeamShow =
                context.resources.getString(R.string.num_team_show, team.num.toString())

            itemView.findViewById<TextView>(R.id.item_result_txt).text = numTeamShow
            innerPlayersAdapter.setItems(team.playerList)
        }
    }
}

class InnerPlayersAdapter(private val context: Context, private val showPosition: Boolean) :
    RecyclerView.Adapter<InnerPlayersAdapter.PlayerViewHolder>() {
    private val players = mutableListOf<PlayerEntity>()

    fun setItems(items: List<PlayerEntity>) {
        players.clear()
        players.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rv_result_player, parent, false)
        )
    }

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(players[position])
    }

    inner class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(player: PlayerEntity) {

            itemView.findViewById<TextView>(R.id.item_result_txt_name).text = player.name
            val txtPos = itemView.findViewById<TextView>(R.id.item_result_txt_position)
            if (showPosition) {
                val positions = context.resources.getStringArray(R.array.positionsPlayer)
                val positionPlayer = positions[player.positionPlayer]
                val positionShow = context.resources.getString(R.string.position_show, positionPlayer)
                txtPos.text = positionShow
            } else {
                txtPos.visibility = View.GONE
            }
        }
    }

}

