package co.geisyanne.meuapp.domain.model

data class Group(
    val name: String,
    val playerList: List<Player>?,
    var selected: Boolean = false
)
