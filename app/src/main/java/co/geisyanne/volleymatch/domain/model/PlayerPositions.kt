package co.geisyanne.volleymatch.domain.model

import co.geisyanne.volleymatch.data.local.entity.PlayerEntity

data class PlayerPositions(
    val positions: List<PlayerListWithCount>,
    val noPosition: MutableList<PlayerEntity>,
    val countCompleteTeams: Int
)

data class PlayerListWithCount(
    val players: MutableList<PlayerEntity>,
    var count: Int // qtd de jogadores dessa posição em um time
)