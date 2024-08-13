package co.geisyanne.volleymatch.presentation.drawTeams.result

import androidx.lifecycle.ViewModel
import co.geisyanne.volleymatch.data.local.entity.PlayerEntity
import co.geisyanne.volleymatch.data.local.entity.TeamEntity
import co.geisyanne.volleymatch.domain.model.PlayerListWithCount
import co.geisyanne.volleymatch.domain.model.PlayerPositions
import co.geisyanne.volleymatch.domain.model.Team
import kotlin.random.Random


class ResultViewModel : ViewModel() {

    private var teamsList: MutableList<TeamEntity> = mutableListOf()
    private var remainingPlayers: MutableList<PlayerEntity> = mutableListOf()
    private var startingTeam: Int = 0
    // private var countCompleteTeams: Int = 0

    fun drawTeams(
        players: List<PlayerEntity>,
        qtdPlayer: Int,
        pos: Boolean,
        lvl: Boolean
    ): MutableList<TeamEntity> {

        teamsList.clear()
        remainingPlayers.clear()

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

    // # SORTEIO COM POSIÇÃO E COM NÍVEL
    private fun drawWithPosWithLvl(players: List<PlayerEntity>, qtdPlayer: Int) {

        val qtdTeams = players.windowed(qtdPlayer, qtdPlayer, true).count()

        // filtrar posições dos jogadores
        val (positionsList, noPosition, qtdCompleteTeams) = getPlayersByPosition(players)

        // ordem para distribuir em os jogadores por nível: decrescente (1) e crescente (2)
        val order = when (Random.nextInt(1, 5)) {
            1 -> listOf(1, 1, 2, 2, 1)
            2 -> listOf(1, 1, 2, 1, 2)
            3 -> listOf(2, 2, 1, 1, 2)
            4 -> listOf(2, 2, 1, 2, 1)
            else -> listOf(1, 1, 2, 2, 1)
        }

        val listCompleteTeams = MutableList(qtdCompleteTeams) { mutableListOf<PlayerEntity>() }

        // distribuição do jogadores com posições definidas
        positionsList.forEachIndexed { i, position ->
            distributePlayersWithPosWithLvl(
                qtdCompleteTeams,
                position.qtdPlayerPos,
                position.players,
                listCompleteTeams,
                order[i]
            )
        }

        // qtd de times incompletos e distribuir jogadores sem posição
        val incompleteTeams = qtdTeams - qtdCompleteTeams
        val listIncomTeams = MutableList(incompleteTeams) { mutableListOf<PlayerEntity>() }
        if (incompleteTeams > 0) {
            distributeRemainingWithPosWithLvl(
                noPosition,
                listIncomTeams,
                qtdPlayer
            )
        }

        // embaralhar os times
        if (listCompleteTeams.size > 1) listCompleteTeams.shuffle()
        val teams = (listCompleteTeams + listIncomTeams).toMutableList()
        saveTeams(teams)
    }

    private fun distributePlayersWithPosWithLvl( // baseia-se na ordenação para equilibrar times
        qtdCompleteTeams: Int,
        qtdPlayerPos: Int,
        players: MutableList<PlayerEntity>,
        teams: MutableList<MutableList<PlayerEntity>>,
        order: Int
    ) {
        if (players.isEmpty()) return  // se não tiver a posição de jogador

        repeat(qtdPlayerPos) {
            when (order) {
                1 -> distributeLvlDec(qtdCompleteTeams, players, teams)
                2 -> distributeLvlAsc(qtdCompleteTeams, players, teams)
            }

            // garantir distribuição cíclica entre os times
            startingTeam += 1
            if (startingTeam >= qtdCompleteTeams) {
                startingTeam = 0
            }
        }

        remainingPlayers.addAll(players)
    }

    // distribuir os jogadores em nível decrescente
    private fun distributeLvlDec(
        qtdCompleteTeams: Int,
        players: MutableList<PlayerEntity>,
        teams: MutableList<MutableList<PlayerEntity>>
    ) {
        for (i in startingTeam until startingTeam + qtdCompleteTeams) {
            val teamIndex =
                i % qtdCompleteTeams //  índice real do time

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

    // distribuir os jogadores em nível crescente
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

    // buscar um jogador de acordo com o nível, a partir do início ou fim da lista - aleatoriamente
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

    // distribuir os jogadores restantes
    private fun distributeRemainingWithPosWithLvl(
        playersNoPos: MutableList<PlayerEntity>,
        teams: MutableList<MutableList<PlayerEntity>>,
        qtdPlayer: Int
    ) {

        val remSetters = remainingPlayers.filter { it.positionPlayer == 1 }.toMutableList()

        teams.forEach { team ->
            if (remSetters.isNotEmpty()) {
                team.add(remSetters.removeAt(remSetters.size - 1))
            }
        }

        remainingPlayers.addAll(remSetters)
        remainingPlayers.addAll(playersNoPos)

        remainingPlayers = remainingPlayers.sortedBy { it.level }.toMutableList()

        for (player in remainingPlayers) {
            for (team in teams) {
                if (team.size < qtdPlayer) {
                    team.add(player)
                    break
                }
            }
        }
    }


    // # SORTEIO SEM POSIÇÃO E COM NÍVEL
    private fun drawNoPosWithLvl(players: List<PlayerEntity>, qtdPlayer: Int) {
        val qtdTeams = players.windowed(qtdPlayer, qtdPlayer, true).count()
        val qtdCompleteTeams = players.windowed(qtdPlayer, qtdPlayer, false).count()
        val allTeams = MutableList(qtdTeams) { mutableListOf<PlayerEntity>() }

        val setters =
            players.filter { it.positionPlayer == 1 }.shuffled().take(qtdTeams).toMutableList()
        val otherPlayers = players.filterNot { it in setters }.toMutableList()

        // add levantador
        allTeams.forEach { team ->
            if (setters.isNotEmpty()) {
                team.add(setters.removeAt(setters.size - 1))
            }
        }

        val playersLow = otherPlayers.filter { it.level < 3 }.shuffled().toMutableList()
        val playersMed = otherPlayers.filter { it.level in 3..4 }.shuffled().toMutableList()
        val playersHigh = otherPlayers.filter { it.level == 5 }.shuffled().toMutableList()

        val playersLvl = listOf(playersHigh, playersMed, playersLow)
        distributeNoPosWithLvl(
            qtdCompleteTeams,
            qtdPlayer,
            playersLvl,
            allTeams,
        )

        val allTeamsShuffled = allTeams
            .shuffled()
            .sortedByDescending { it.size == qtdPlayer }
            .toMutableList()

        saveTeams(allTeamsShuffled)
    }

    private fun distributeNoPosWithLvl(
        qtdCompleteTeams: Int,
        qtdPlayer: Int,
        playersLvl: List<MutableList<PlayerEntity>>,
        teams: MutableList<MutableList<PlayerEntity>>
    ) {

        while (!checkCompleteTeams(teams, qtdPlayer, qtdCompleteTeams)) {
            for (playerLvl in playersLvl) {
                if (playerLvl.isNotEmpty()) {
                    tryAddPlayers(playerLvl, teams, qtdPlayer, qtdCompleteTeams)
                }
            }
        }

        val remainingPlayers = mutableListOf<PlayerEntity>().apply {
            playersLvl.forEach { addAll(it) }
        }

        if (remainingPlayers.isNotEmpty()) {
            for (player in remainingPlayers) {
                for (team in teams) {
                    if (team.size < qtdPlayer) {
                        team.add(player)
                        break
                    }
                }
            }
        }
    }

    private fun tryAddPlayers(
        playersLvl: MutableList<PlayerEntity>,
        teams: MutableList<MutableList<PlayerEntity>>,
        qtdPlayer: Int,
        qtdCompleteTeams: Int
    ) {

        if (playersLvl.isNotEmpty()) {
            for (i in 0 until qtdCompleteTeams) {
                if (playersLvl.isNotEmpty() && teams[i].size < qtdPlayer) {
                    teams[i].add(playersLvl.removeAt(playersLvl.size - 1))
                }
            }
        }
    }

    private fun checkCompleteTeams(
        teams: MutableList<MutableList<PlayerEntity>>,
        qtdPlayer: Int,
        qtdCompleteTeams: Int
    ): Boolean {
        return teams.count { it.size == qtdPlayer } == qtdCompleteTeams
    }

    /* private fun checkCompleteDistribution(
         teams: MutableList<MutableList<PlayerEntity>>,
         qtdPlayer: Int,
         qtdCompleteTeams: Int,
         qtdAllPlayers: Int
     ): Boolean {
         return teams.sumBy { it.size } != qtdAllPlayers
     }*/

    /*        // criar times
            for (i in 0 until qtdTeams) {
                val playersList = mutableListOf<PlayerEntity>()
                var qtdPlayerLoop = qtdPlayer

                // add levantador
                if (setters.isNotEmpty()) {
                    playersList.add(setters.removeAt(setters.size - 1))
                    qtdPlayerLoop -= 1
                }

                // distribuir os jogadores
                for (j in 0 until qtdPlayerLoop) {
                    when (j % 3) { // j de 0 a 2 até formar o time
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
                allTeams.add(playersList)
            }*/



    //  # SORTEIO COM POSIÇÃO E SEM NÍVEL
    private fun drawWithPosNoLvl(players: List<PlayerEntity>, qtdPlayer: Int) {

        val qtdTeams = players.windowed(qtdPlayer, qtdPlayer, true).count()
        val teams = MutableList(qtdTeams) { mutableListOf<PlayerEntity>() }

        // filtrar posições dos jogadores
        val (positionsList, noPosition, qtdCompleteTeams) = getPlayersByPosition(players)

        // distribuir jogadores com posições definidas
        positionsList.forEach { position ->
            distributePlayersWithPosNoLvl(
                qtdCompleteTeams,
                position.qtdPlayerPos,
                position.players,
                teams
            )
        }

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
        remainingPlayers.addAll(players)
    }

    private fun distributeRemaining(
        qtdTeams: Int,
        players: MutableList<PlayerEntity>, // list noPosition
        teams: MutableList<MutableList<PlayerEntity>>,
        qtdPlayer: Int
    ) {
        remainingPlayers.addAll(players)
        remainingPlayers.shuffle()

        for (i in 0 until qtdTeams) {
            while (teams[i].size < qtdPlayer && remainingPlayers.isNotEmpty()) {
                teams[i].add(remainingPlayers.removeAt(remainingPlayers.size - 1))
            }
        }
    }


    //  # SORTEIO SEM POSIÇÃO E SEM NÍVEL
    private fun drawNoPosNoLvl(players: List<PlayerEntity>, qtdPlayer: Int) {

        val qtdTeams = players.windowed(qtdPlayer, qtdPlayer, true).count()
        val completeTeams = mutableListOf<MutableList<PlayerEntity>>()
        val incompleteTeams = mutableListOf<MutableList<PlayerEntity>>()
        val allTeams = mutableListOf<MutableList<PlayerEntity>>()

        val setters = players.filter { it.positionPlayer == 1 }.shuffled().toMutableList()
        val otherPlayers = players.filter { it.positionPlayer != 1 }.shuffled().toMutableList()

        for (i in 0 until qtdTeams) {
            val playersList = mutableListOf<PlayerEntity>()

            if (setters.isNotEmpty()) {
                //  remove o último setter da sua lista e o adc a playersList
                // a partir do final da lista (ordem inversa) pra não afetar os indices dos elementos restantes
                playersList.add(setters.removeAt(setters.size - 1))

                // add outros jogadores
                for (j in 0 until (qtdPlayer - 1)) {
                    if (otherPlayers.isNotEmpty()) {
                        playersList.add(otherPlayers.removeAt(otherPlayers.size - 1))
                    } else if (setters.isNotEmpty()) { // se só restar setters
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
            // alterar ordem apenas dos times completos
            if (playersList.size == qtdPlayer) {
                completeTeams.add(playersList)
            } else {
                incompleteTeams.add(playersList)
            }
        }

        allTeams.addAll(completeTeams.shuffled().toMutableList())
        allTeams.addAll(incompleteTeams)
        saveTeams(allTeams)
    }

    private fun getPlayersByPosition(players: List<PlayerEntity>): PlayerPositions {
        val setters = PlayerListWithCount(
            players.filter { it.positionPlayer == 1 }.shuffled()
                .toMutableList(), qtdPlayerPos = 1
        )
        val outsides = PlayerListWithCount(
            players.filter { it.positionPlayer == 2 }.shuffled()
                .toMutableList(), qtdPlayerPos = 2
        )
        val opposites = PlayerListWithCount(
            players.filter { it.positionPlayer == 3 }.shuffled()
                .toMutableList(), qtdPlayerPos = 1
        )
        val middles = PlayerListWithCount(
            players.filter { it.positionPlayer == 4 }.shuffled()
                .toMutableList(), qtdPlayerPos = 2
        )
        val liberos = PlayerListWithCount(
            players.filter { it.positionPlayer == 5 }.shuffled()
                .toMutableList(), qtdPlayerPos = 1
        )
        val noPosition = players.filter { it.positionPlayer == 0 }.shuffled().toMutableList()

        // qtd de times completos
        val countCompleteTeams = minOf(
            setters.players.size,
            outsides.players.size / 2,
            opposites.players.size,
            middles.players.size / 2
        )

        val positionsList = listOf(setters, outsides, opposites, middles, liberos)

        return PlayerPositions(positionsList, noPosition, countCompleteTeams)
    }

    private fun saveTeams(teams: MutableList<MutableList<PlayerEntity>>) {
        for (i in teams.indices) {
            if (teams[i].isEmpty()) return
            val team = TeamEntity(
                num = (i + 1),
                playerList = teams[i]
            )
            teamsList.add(team)
        }
    }

}
