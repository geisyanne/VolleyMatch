package co.geisyanne.meuapp.presentation.drawTeams.result

import androidx.lifecycle.ViewModel
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.domain.model.Team

class ResultViewModel : ViewModel() {

    private var teamsList: MutableList<Team> = mutableListOf()

    fun drawTeams(
        players: List<PlayerEntity>,
        qtdPlayer: Int,
        pos: Boolean,
        lvl: Boolean
    ): MutableList<Team> {
        if (qtdPlayer < 1) {
            throw IllegalArgumentException("Número inválido de jogadores por time")
        }

        when {
            pos && lvl -> drawWithPosWithLvl(players, qtdPlayer)
            !pos && lvl -> drawNoPosWithLvl(players, qtdPlayer)
            pos && !lvl -> drawWithPosNoLvl(players, qtdPlayer)
            else -> drawNoPosNoLvl(players, qtdPlayer)
        }
        return teamsList
    }

    // sorteio com posição e com nível
    private fun drawWithPosWithLvl(players: List<PlayerEntity>, qtdPlayer: Int) {
        // TODO
    }

    // sorteio sem posição e com nível
    private fun drawNoPosWithLvl(players: List<PlayerEntity>, qtdPlayer: Int) {

        val qtdTeams = players.windowed(qtdPlayer, qtdPlayer, true).count()
        val lifters = players.filter { it.positionPlayer == 1 }.shuffled()
        val otherPlayers = players.filter { it.positionPlayer != 1 }.shuffled()
        val sortedPlayers = otherPlayers.sortedByDescending {it.level}
        val teams = MutableList(qtdTeams) { mutableListOf<PlayerEntity>() }

        distributePlayersEquallyAmongTeams(lifters, teams, qtdPlayer)
        distributePlayersEquallyAmongTeams(sortedPlayers, teams, qtdPlayer)

        saveTeams(teams)
    }

    private fun distributePlayersEquallyAmongTeams(
        players: List<PlayerEntity>,
        teams: MutableList<MutableList<PlayerEntity>>,
        qtdPlayer: Int
    ) {
        for (i in players.indices) { // percorre cada jogador
            val teamIndex = i % teams.size // indice dos times cíclico
            if (teams[teamIndex].size < qtdPlayer) {
                teams[teamIndex].add(players[i])
            }
        }
    }

    // sorteio com posição e sem nível
    private fun drawWithPosNoLvl(players: List<PlayerEntity>, qtdPlayer: Int) {
        // TODO
    }

    // sorteio sem posição e sem nível
    private fun drawNoPosNoLvl(players: List<PlayerEntity>, qtdPlayer: Int) {

        val qtdTeams = players.windowed(qtdPlayer, qtdPlayer, true).count()

        val lifters =
            players.filter { it.positionPlayer == 1 }.shuffled()
                .toMutableList() // filtrando levantadores
        val otherPlayers = players.filter { it.positionPlayer != 1 }.shuffled().toMutableList()

        for (i in 0 until qtdTeams) {
            val playersList = mutableListOf<PlayerEntity>()

            if (lifters.isNotEmpty()) {
                // add o último levantador a playerList e já remove o mesmo de lifters
                // a partir do final da lista (ordem inversa) pra não afetar os indices dos elementos restantes
                playersList.add(lifters.removeAt(lifters.size - 1))

                // add outros jogadores
                for (j in 0 until (qtdPlayer - 1)) {
                    if (otherPlayers.isNotEmpty()) {
                        playersList.add(otherPlayers.removeAt(otherPlayers.size - 1))
                    } else if (lifters.isNotEmpty()) {
                        playersList.add(lifters.removeAt(lifters.size - 1))
                    }
                }
            } else {
                // se não houver levantadores
                for (j in 0 until qtdPlayer) {
                    if (otherPlayers.isNotEmpty()) {
                        playersList.add(otherPlayers.removeAt(otherPlayers.size - 1))
                    }
                }
            }
            saveTeam(i + 1, playersList)
        }
    }

    private fun saveTeam(teamNumber: Int, playerList: List<PlayerEntity>) {
        val team = Team(
            num = teamNumber,
            playerList = playerList
        )
        teamsList.add(team)
    }

    private fun saveTeams(teams: MutableList<MutableList<PlayerEntity>>) {
        for ( i in teams.indices) {
            val team = Team(
                num = (i+1),
                playerList = teams[i]
            )
            teamsList.add(team)
        }
    }


}
