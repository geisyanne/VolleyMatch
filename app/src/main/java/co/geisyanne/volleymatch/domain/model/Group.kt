package co.geisyanne.volleymatch.domain.model

data class Group(
    val name: String,
    val playerList: List<Player>?,
    var selected: Boolean = false
)
