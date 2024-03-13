package co.geisyanne.volleymatch.presentation.drawTeams.result

import androidx.lifecycle.ViewModel
import co.geisyanne.volleymatch.data.local.entity.PlayerEntity
import co.geisyanne.volleymatch.domain.model.Team
import kotlin.random.Random


class ResultViewModel : ViewModel() {

    private var teamsList: MutableList<Team> = mutableListOf()
    private var remaining: MutableList<PlayerEntity> = mutableListOf()
    private var startingTeam: Int = 0

    fun drawTeams(
        players: List<PlayerEntity>,
        qtdPlayer: Int,
        pos: Boolean,
        lvl: Boolean
    ): MutableList<Team> {

        teamsList.clear()
        remaining.clear()

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
        //val lastTeam = players.windowed(qtdPlayer, qtdPlayer, true).last().size
        //val teams = MutableList(qtdTeams) { mutableListOf<PlayerEntity>() }

        val setters =
            players.filter { it.positionPlayer == 1 }.shuffled().toMutableList() //  1/TEAM
        val outsides =
            players.filter { it.positionPlayer == 2 }.shuffled().toMutableList()  // 2/TEAM
        val opposites =
            players.filter { it.positionPlayer == 3 }.shuffled().toMutableList() // 1/TEAM
        val middles = players.filter { it.positionPlayer == 4 }.shuffled().toMutableList() // 2/TEAM

        val liberos =
            players.filter { it.positionPlayer == 5 }.shuffled().toMutableList() // 0 OU 1/TEAM

        val noPosition = players.filter { it.positionPlayer == 0 }.toMutableList()

        val settersCount = setters.size
        val outsidesCount = outsides.size
        val oppositesCount = opposites.size
        val middlesCount = middles.size

        // Qtd de times completos
        val completeTeams = minOf(
            settersCount,
            outsidesCount / 2,
            oppositesCount,
            middlesCount / 2
        )

        // Distribuição aletória em ordem decrescente/crescente
        val order = when (Random.nextInt(1, 5)) {
            1 -> listOf(1, 1, 2, 2, 1)
            2 -> listOf(1, 1, 2, 1, 2)
            3 -> listOf(2, 2, 1, 1, 2)
            4 -> listOf(2, 2, 1, 2, 1)
            else -> listOf(1, 1, 2, 2, 1)
        }

        val listCompTeams = MutableList(completeTeams) { mutableListOf<PlayerEntity>() }

        // Distribuição para times completos
        distributePlayersWithPosWithLvl(completeTeams, 1, setters, listCompTeams, order[0])
        distributePlayersWithPosWithLvl(completeTeams, 2, outsides, listCompTeams, order[1])
        distributePlayersWithPosWithLvl(completeTeams, 2, middles, listCompTeams, order[2])
        distributePlayersWithPosWithLvl(completeTeams, 1, opposites, listCompTeams, order[3])
        distributePlayersWithPosWithLvl(completeTeams, 1, liberos, listCompTeams, order[4])

        // Qtd de times incompletos
        val incompleteTeams = qtdTeams - completeTeams
        val listIncomTeams = MutableList(incompleteTeams) { mutableListOf<PlayerEntity>() }
        if (incompleteTeams > 0) {
            distributeRemainingWithPosWithLvl(
                incompleteTeams,
                noPosition,
                listIncomTeams,
                qtdPlayer
            )
        }

        if (listCompTeams.size > 1) listCompTeams.shuffle()
        val teams = (listCompTeams + listIncomTeams).toMutableList()
        saveTeams(teams)
    }

    private fun distributePlayersWithPosWithLvl(
        qtdTeams: Int,
        qtdPlayerPos: Int,
        players: MutableList<PlayerEntity>,
        teams: MutableList<MutableList<PlayerEntity>>,
        order: Int
    ) {
        if (players.isEmpty()) return  // se não tiver a posição de jogador

        repeat(qtdPlayerPos) {
            when (order) {
                1 -> distributeLvlDec(qtdTeams, players, teams)
                2 -> distributeLvlAsc(qtdTeams, players, teams)
            }
            startingTeam += 1
            if (startingTeam >= qtdTeams) {
                startingTeam = 0
            }
        }
        remaining.addAll(players)
    }

    private fun distributeLvlDec(
        qtdTeams: Int, players: MutableList<PlayerEntity>,
        teams: MutableList<MutableList<PlayerEntity>>
    ) {
        for (i in startingTeam until startingTeam + qtdTeams) {
            val teamIndex = i % qtdTeams // Calculando o índice real do time

            if (players.isEmpty()) return

            val playerHigh = findPlayerWithLevel(players, 5..5)
            if (playerHigh != null) {
                teams[teamIndex].add(playerHigh)
                players.remove(playerHigh)
            } else {
                val playerMed = findPlayerWithLevel(players, 3..4)
                if (playerMed != null) {
                    teams[teamIndex].add(playerMed)
                    players.remove(playerMed)
                } else {
                    val playerLow = findPlayerWithLevel(players, 0..3)
                    if (playerLow != null) {
                        teams[teamIndex].add(playerLow)
                        players.remove(playerLow)
                    }
                }
            }
        }
    }

    private fun distributeLvlAsc(
        qtdTeams: Int, players: MutableList<PlayerEntity>,
        teams: MutableList<MutableList<PlayerEntity>>
    ) {
        for (i in startingTeam until startingTeam + qtdTeams) {
            val teamIndex = i % qtdTeams // Calculando o índice real do time

            if (players.isEmpty()) return

            val playerLow = findPlayerWithLevel(players, 0..3)
            if (playerLow != null) {
                teams[teamIndex].add(playerLow)
                players.remove(playerLow)
            } else {
                val playerMed = findPlayerWithLevel(players, 3..4)
                if (playerMed != null) {
                    teams[teamIndex].add(playerMed)
                    players.remove(playerMed)
                } else {
                    val playerHigh = findPlayerWithLevel(players, 5..5)
                    if (playerHigh != null) {
                        teams[teamIndex].add(playerHigh)
                        players.remove(playerHigh)
                    }

                }
            }
        }
    }

    private fun findPlayerWithLevel(
        players: List<PlayerEntity>,
        levelRange: IntRange
    ): PlayerEntity? {
        return if (Random.nextBoolean()) {
            players.firstOrNull { it.level in levelRange }
        } else {
            players.lastOrNull { it.level in levelRange }
        }
    }

    private fun distributeRemainingWithPosWithLvl(
        qtdTeams: Int,
        playersNoPos: MutableList<PlayerEntity>,
        teams: MutableList<MutableList<PlayerEntity>>,
        qtdPlayer: Int
    ) {
        remaining.addAll(playersNoPos)
        remaining = remaining.sortedBy { it.level }.toMutableList()

        for (player in remaining) {
            for (team in teams) {
                if (team.size < qtdPlayer) {
                    team.add(player)
                    break
                }
            }
        }
    }


    // sorteio sem posição e com nível
    private fun drawNoPosWithLvl(players: List<PlayerEntity>, qtdPlayer: Int) {
        val qtdTeams = players.windowed(qtdPlayer, qtdPlayer, true).count()

        val setters = players.filter { it.positionPlayer == 1 }.shuffled().toMutableList()
        val settersByTeam = setters.take(qtdTeams).toMutableList()
        setters.removeAll(settersByTeam)

        val otherPlayers = players.filter { it.positionPlayer != 1 }.toMutableList()
        otherPlayers.addAll(setters)

        val playersLow = otherPlayers.filter { it.level < 3 }.shuffled().toMutableList()
        val playersMed = otherPlayers.filter { it.level in 3..4 }.shuffled().toMutableList()
        val playersHigh = otherPlayers.filter { it.level == 5 }.shuffled().toMutableList()

        for (i in 0 until qtdTeams) {
            val playersList = mutableListOf<PlayerEntity>()
            var qtdPlayerLoop = qtdPlayer

            // add levantador
            if (settersByTeam.isNotEmpty()) {
                playersList.add(settersByTeam.removeAt(settersByTeam.size - 1))
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
