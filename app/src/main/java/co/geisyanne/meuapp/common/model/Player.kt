package co.geisyanne.meuapp.common.model

data class Player(
    val name: String,
    val position: String?,
    val level: Int?,
    val group: Group?,
    var selected: Boolean = false
)