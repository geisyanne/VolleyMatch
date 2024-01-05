package co.geisyanne.meuapp.common.model

data class Group(
    val name: String,
    val players: List<Player>?,
    var selected: Boolean = false
)
