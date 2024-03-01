package co.geisyanne.meuapp.presentation.drawTeams.result

import androidx.lifecycle.ViewModel
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.domain.model.Team

class ResultViewModel : ViewModel() {

    private var teamsList: MutableList<Team> = mutableListOf()
    private var remaining: MutableList<PlayerEntity> = mutableListOf()

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

        val qtdTeams = players.windowed(qtdPlayer, qtdPlayer, true).count()
        val lastTeam = players.windowed(qtdPlayer, qtdPlayer, true).last().size
        val teams = MutableList(qtdTeams) { mutableListOf<PlayerEntity>() }

        val setters =
            players.filter { it.positionPlayer == 1 }.sortedByDescending { it.level }
                .toMutableList() //  1/TEAM
        val outsides =
            players.filter { it.positionPlayer == 2 }.sortedBy { it.level }
                .toMutableList()  // 2/TEAM
        val opposites =
            players.filter { it.positionPlayer == 3 }.sortedByDescending { it.level }
                .toMutableList() // 1/TEAM
        val middles = players.filter { it.positionPlayer == 4 }.sortedBy { it.level }
            .toMutableList() // 2/TEAM

        val liberos =
            players.filter { it.positionPlayer == 5 }.sortedByDescending { it.level }
                .toMutableList() // 0 OU 1/TEAM

        val noPosition =
            players.filter { it.positionPlayer == 0 }.sortedBy { it.level }.toMutableList()

        distributePlayersWithPosWithLvl(qtdTeams, 1, setters, teams)
        distributePlayersWithPosWithLvl(qtdTeams, 2, outsides, teams)
        distributePlayersWithPosWithLvl(qtdTeams, 1, opposites, teams)
        distributePlayersWithPosWithLvl(qtdTeams, 2, middles, teams)

        distributePlayersWithPosWithLvl(qtdTeams, 1, liberos, teams)

        distributeRemainingWithPosWithLvl(qtdTeams, noPosition, teams, qtdPlayer, lastTeam)

        saveTeams(teams)
    }

    private fun distributePlayersWithPosWithLvl(
        qtdTeams: Int,
        qtdPlayerPos: Int,
        players: MutableList<PlayerEntity>,
        teams: MutableList<MutableList<PlayerEntity>>
    ) {
        if (players.isEmpty()) return  // se não tiver a posição de jogador

        repeat(qtdPlayerPos) {
            for (i in 0 until qtdTeams) {
                if (players.isNotEmpty()) {
                    val player = players.removeAt(players.size - 1)
                    teams[i].add(player)
                }
            }
        }

        remaining.addAll(players)
    }

    private fun distributeRemainingWithPosWithLvl(
        qtdTeams: Int,
        playersNoPos: MutableList<PlayerEntity>,
        teams: MutableList<MutableList<PlayerEntity>>,
        qtdPlayer: Int,
        lastTeam: Int,
    ) {
        remaining.addAll(playersNoPos)
        remaining.sortedBy { it.level }

        for (i in remaining) {

            for (indexTeam in 0 until qtdTeams) {

                if (indexTeam == qtdTeams - 1) {
                    if (teams[indexTeam].size > lastTeam) {
                        return
                    }
                }

                if (teams[indexTeam].size < qtdPlayer && remaining.isNotEmpty()) {
                    teams[indexTeam].add(remaining.removeAt(remaining.size - 1))
                }

            }
        }
    }


    // sorteio sem posição e com nível
    private fun drawNoPosWithLvl(players: List<PlayerEntity>, qtdPlayer: Int) {
        val qtdTeams = players.windowed(qtdPlayer, qtdPlayer, true).count()

        val allSetters = players.filter { it.positionPlayer == 1 }.shuffled().toMutableList()
        val setters = allSetters.take(qtdTeams).toMutableList()
        allSetters.removeAll(setters)

        val otherPlayers = players.filter { it.positionPlayer != 1 }.toMutableList()
        otherPlayers.addAll(allSetters)
        otherPlayers.shuffle()

        val playersLow = otherPlayers.filter { it.level < 3 }.shuffled().toMutableList()
        val playersMed = otherPlayers.filter { it.level in 3..4 }.shuffled().toMutableList()
        val playersHigh = otherPlayers.filter { it.level == 5 }.shuffled().toMutableList()

        for (i in 0 until qtdTeams) {
            val playersList = mutableListOf<PlayerEntity>()
            var qtdPlayerLoop = qtdPlayer

            // add levantador
            if (setters.isNotEmpty()) {
                playersList.add(setters.removeAt(setters.size - 1))
                qtdPlayerLoop -= 1
            }

            for (j in 0 until qtdPlayerLoop) {
                when (j % 3) {
                    0 -> {
                        if (playersLow.isNotEmpty()) {
                            playersList.add(playersLow.removeAt(playersLow.size - 1))
                        } else if (playersHigh.isNotEmpty()) {
                            playersList.add(playersHigh.removeAt(playersHigh.size - 1))
                        } else if (playersMed.isNotEmpty()) {
                            playersList.add(playersMed.removeAt(playersMed.size - 1))
                        }
                    }

                    1 -> {
                        if (playersMed.isNotEmpty()) {
                            playersList.add(playersMed.removeAt(playersMed.size - 1))
                        } else if (playersHigh.isNotEmpty()) {
                            playersList.add(playersHigh.removeAt(playersHigh.size - 1))
                        } else if (playersLow.isNotEmpty()) {
                            playersList.add(playersLow.removeAt(playersLow.size - 1))
                        }
                    }

                    2 -> {
                        if (playersHigh.isNotEmpty()) {
                            playersList.add(playersHigh.removeAt(playersHigh.size - 1))
                        } else if (playersMed.isNotEmpty()) {
                            playersList.add(playersMed.removeAt(playersMed.size - 1))
                        } else if (playersLow.isNotEmpty()) {
                            playersList.add(playersLow.removeAt(playersLow.size - 1))
                        }
                    }
                }
            }
            saveTeam(i + 1, playersList)
        }
    }


    // sorteio com posição e sem nível
    private fun drawWithPosNoLvl(players: List<PlayerEntity>, qtdPlayer: Int) {

        val qtdTeams = players.windowed(qtdPlayer, qtdPlayer, true).count()
        val teams = MutableList(qtdTeams) { mutableListOf<PlayerEntity>() }

        val setters =
            players.filter { it.positionPlayer == 1 }.shuffled().toMutableList() //  1/TEAM
        val outsides =
            players.filter { it.positionPlayer == 2 }.shuffled().toMutableList()  // 2/TEAM
        val opposites =
            players.filter { it.positionPlayer == 3 }.shuffled().toMutableList() // 1/TEAM
        val middles = players.filter { it.positionPlayer == 4 }.shuffled().toMutableList() // 2/TEAM

        val liberos =
            players.filter { it.positionPlayer == 5 }.shuffled().toMutableList() // 0 OU 1/TEAM

        val noPosition = players.filter { it.positionPlayer == 0 }.shuffled().toMutableList()

        distributePlayersWithPosNoLvl(qtdTeams, 1, setters, teams)
        distributePlayersWithPosNoLvl(qtdTeams, 2, outsides, teams)
        distributePlayersWithPosNoLvl(qtdTeams, 1, opposites, teams)
        distributePlayersWithPosNoLvl(qtdTeams, 2, middles, teams)

        distributePlayersWithPosNoLvl(qtdTeams, 1, liberos, teams)

        distributeRemaining(qtdTeams, noPosition, teams, qtdPlayer)

        saveTeams(teams)
    }

    private fun distributePlayersWithPosNoLvl(
        qtdTeams: Int,
        qtdPlayerPos: Int,
        players: MutableList<PlayerEntity>,
        teams: MutableList<MutableList<PlayerEntity>>
    ) {
        if (players.isEmpty()) return

        for (i in 0 until qtdTeams) {
            repeat(qtdPlayerPos) {
                if (players.isNotEmpty()) {
                    val player = players.removeAt(players.size - 1)
                    teams[i].add(player)
                }
            }
        }
        remaining.addAll(players)
    }

    private fun distributeRemaining(
        qtdTeams: Int,
        players: MutableList<PlayerEntity>,
        teams: MutableList<MutableList<PlayerEntity>>,
        qtdPlayer: Int
    ) {
        remaining.addAll(players)
        remaining.shuffle()

        for (i in 0 until qtdTeams) {
            while (teams[i].size < qtdPlayer && remaining.isNotEmpty()) {
                teams[i].add(remaining.removeAt(remaining.size - 1))
            }
        }
    }


    // sorteio sem posição e sem nível
    private fun drawNoPosNoLvl(players: List<PlayerEntity>, qtdPlayer: Int) {

        val qtdTeams = players.windowed(qtdPlayer, qtdPlayer, true).count()

        val setters =
            players.filter { it.positionPlayer == 1 }.shuffled()
                .toMutableList() // filtrando levantadores
        val otherPlayers = players.filter { it.positionPlayer != 1 }.shuffled().toMutableList()

        for (i in 0 until qtdTeams) {
            val playersList = mutableListOf<PlayerEntity>()

            if (setters.isNotEmpty()) {
                // add o último levantador a playerList e já remove o mesmo de lifters
                // a partir do final da lista (ordem inversa) pra não afetar os indices dos elementos restantes
                playersList.add(setters.removeAt(setters.size - 1))

                // add outros jogadores
                for (j in 0 until (qtdPlayer - 1)) {
                    if (otherPlayers.isNotEmpty()) {
                        playersList.add(otherPlayers.removeAt(otherPlayers.size - 1))
                    } else if (setters.isNotEmpty()) {
                        playersList.add(setters.removeAt(setters.size - 1))
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
        for (i in teams.indices) {
            if (teams[i].isEmpty()) return
            val sortedTeam = teams[i].sortedBy { it.positionPlayer }
            val team = Team(
                num = (i + 1),
                playerList = sortedTeam
            )
            teamsList.add(team)
        }
    }


}
