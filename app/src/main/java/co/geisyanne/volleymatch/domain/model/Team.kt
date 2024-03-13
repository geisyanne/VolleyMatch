package co.geisyanne.volleymatch.domain.model

import co.geisyanne.volleymatch.data.local.entity.PlayerEntity

data class Team (
    val num: Int,
    val playerList: List<PlayerEntity>
)