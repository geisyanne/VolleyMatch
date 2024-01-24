package co.geisyanne.meuapp.domain.model

data class Player(
    val name: String,
    val position: Int?,
    val level: Int?,
    val group: Group?,
    var selected: Boolean = false
)