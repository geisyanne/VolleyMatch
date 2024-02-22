package co.geisyanne.meuapp.presentation.drawTeams.home

import co.geisyanne.meuapp.data.local.entity.PlayerEntity

interface FragmentAttachListener {

    fun goToRegisterPlayer()

    fun goToUpdatePlayer(player: PlayerEntity?)

    fun goToResult(players: List<PlayerEntity>, qtdPlayer: Int, pos: Boolean, lvl: Boolean)

    //fun goToGroup(groupId: Long)

}