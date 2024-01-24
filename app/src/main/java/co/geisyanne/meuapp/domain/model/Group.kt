package co.geisyanne.meuapp.domain.model

data class Group(
    val name: String,
    val players: List<Player>?,
    var selected: Boolean = false
)
