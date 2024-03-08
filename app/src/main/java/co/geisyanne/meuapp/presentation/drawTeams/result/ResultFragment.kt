package co.geisyanne.meuapp.presentation.drawTeams.result

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.databinding.FragmentResultBinding
import co.geisyanne.meuapp.domain.model.Team
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultFragment : Fragment(R.layout.fragment_result) {

    private var binding: FragmentResultBinding? = null
    private lateinit var viewModel: ResultViewModel

    private lateinit var adapter: ResultAdapter
    private var teams: MutableList<Team> = mutableListOf()

    private var showPosition: Boolean = false
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResultBinding.bind(view)

        viewModel = ViewModelProvider(this)[ResultViewModel::class.java]


        arguments?.let { bundle ->
            val players: ArrayList<PlayerEntity>? = bundle.getParcelableArrayList("KEY_PLAYERS")
            val qtdPlayer: Int = bundle.getInt("KEY_QTD")
            val pos: Boolean = bundle.getBoolean("KEY_POS")
            val lvl: Boolean = bundle.getBoolean("KEY_LVL")

            showPosition = pos
            teams = players?.let { viewModel.drawTeams(it, qtdPlayer, pos, lvl) } ?: mutableListOf()
        }

        binding?.resultRvTeams?.layoutManager = LinearLayoutManager(requireContext())
        adapter = ResultAdapter(requireContext(), teams, showPosition)
        binding?.resultRvTeams?.adapter = adapter

        if (showPosition) {
            checkCompleteTeams()
        }

    }

    private fun checkCompleteTeams() {
        val hasIncompleteTeams = teams.any { it.playerList.size < 6 }
        if (hasIncompleteTeams) {
            snackbar =
                Snackbar.make(
                    binding?.root ?: return,
                    R.string.not_enought_players,
                    5000
                )
                    .setBackgroundTint(resources.getColor(R.color.blue_dark))
            snackbar?.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareResult()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareResult() {
        val resultTxt = generateTeamResultText()
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, resultTxt)
        startActivity(Intent.createChooser(intent, getString(R.string.share_txt)))

    }

    private fun generateTeamResultText(): String {
        val stringBuilder = StringBuilder()

        // date and hour
        val sdfDateTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val currentDateTime = sdfDateTime.format(Date())

        // positions
        val positionsMap = mapOf(
            0 to " ",
            1 to getString(R.string.setter_txt),
            2 to getString(R.string.outside_txt),
            3 to getString(R.string.opposite_txt),
            4 to getString(R.string.middle_txt),
            5 to getString(R.string.libero_txt)
        )

        // teams
        teams.forEachIndexed { index, team ->
            // título
            stringBuilder.append(getString(R.string.team_txt)).append(index + 1)
                .append("\n---------------\n")

            // players
            team.playerList.forEach { player ->
                // name
                stringBuilder.append(player.name).append(" ")

                // position
                if (showPosition) {
                    val position = positionsMap[player.positionPlayer]
                    if (player.positionPlayer != 0) {
                        stringBuilder.append("- ").append(position)
                    }
                }
                stringBuilder.append("\n")
            }
            stringBuilder.append("\n")
        }
        // app info
        stringBuilder.append(getString(R.string.info_app_txt, currentDateTime))
        return stringBuilder.toString()
    }

    override fun onPause() {
        super.onPause()
        snackbar?.dismiss()
    }
}