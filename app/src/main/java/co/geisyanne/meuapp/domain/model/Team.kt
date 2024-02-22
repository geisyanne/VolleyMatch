package co.geisyanne.meuapp.domain.model

import co.geisyanne.meuapp.data.local.entity.PlayerEntity

data class Team (
    val num: Int,
    val playerList: List<PlayerEntity>
)