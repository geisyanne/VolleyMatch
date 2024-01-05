package co.geisyanne.meuapp.common.model

data class Player(
    val uuid: String,
    val name: String,
    val position: String,
    val level: Int,
    val group: Group?,
    var selected: Boolean = false
)