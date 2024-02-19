package co.geisyanne.meuapp.domain.model

data class Player(
    val name: String,
    val positionPlayer: Int?,
    val level: Int?,
    var selected: Boolean = false
)