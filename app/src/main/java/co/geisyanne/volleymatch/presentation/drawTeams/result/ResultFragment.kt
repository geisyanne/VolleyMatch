package co.geisyanne.volleymatch.presentation.drawTeams.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.data.local.entity.PlayerEntity
import co.geisyanne.volleymatch.data.local.entity.TeamEntity
import co.geisyanne.volleymatch.databinding.FragmentResultBinding
import co.geisyanne.volleymatch.presentation.common.extension.getSnackbarColor
import co.geisyanne.volleymatch.presentation.drawTeams.home.FragmentAttachListener
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultFragment : Fragment(R.layout.fragment_result) {

    private val viewModel: ResultViewModel by viewModel()
    private var binding: FragmentResultBinding? = null
    private var fragmentAttachListener: FragmentAttachListener? = null

    //private lateinit var adapter: ResultAdapter
    private var snackbar: Snackbar? = null

    private var players: ArrayList<PlayerEntity>? = null
    private var qtdPlayer: Int = 0
    private var lvl: Boolean = false
    private var teams: MutableList<TeamEntity> = mutableListOf()
    private var showPosition: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResultBinding.bind(view)

        setupArguments()
        setupView()
    }

    private fun setupArguments() {
        arguments?.let { bundle ->
            players = bundle.getParcelableArrayList("KEY_PLAYERS")
            qtdPlayer = bundle.getInt("KEY_QTD")
            showPosition = bundle.getBoolean("KEY_POS")
            lvl = bundle.getBoolean("KEY_LVL")
        }
    }

    private fun setupView() {
        drawAndDisplayTeams()
        if (showPosition) {
            checkCompleteTeams()
        }

        binding?.resultBtnDrawAgain?.setOnClickListener {
            drawAndDisplayTeams()
        }

//        binding?.resultBtnDrawEdit?.setOnClickListener {
//            fragmentAttachListener?.goToEditResult(teams, showPosition)
//        }


    }

    private fun drawAndDisplayTeams() {
        teams = drawTeams(players, qtdPlayer, showPosition, lvl)
        binding?.resultRvTeams?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ResultAdapter(requireContext(), teams, showPosition)
        }
    }

    private fun drawTeams(
        players: ArrayList<PlayerEntity>?,
        qtdPlayer: Int,
        pos: Boolean,
        lvl: Boolean
    ): MutableList<TeamEntity> {
        return players?.let { viewModel.drawTeams(it, qtdPlayer, pos, lvl) } ?: mutableListOf()
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
                    .setBackgroundTint(ContextCompat.getColor(requireContext(), getSnackbarColor()))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
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
            android.R.id.home -> {
                requireActivity().supportFragmentManager.popBackStack()
                true
            }

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

    // CHECK: IF THE ACTIVITY IMPLEMENTS AN INTERFACE
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentAttachListener) {
            fragmentAttachListener = context
        }
    }

    override fun onPause() {
        super.onPause()
        snackbar?.dismiss()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}