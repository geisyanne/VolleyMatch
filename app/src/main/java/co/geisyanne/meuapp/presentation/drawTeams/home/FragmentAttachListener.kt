package co.geisyanne.meuapp.presentation.drawTeams.home

import co.geisyanne.meuapp.data.local.entity.PlayerEntity

interface FragmentAttachListener {

    fun goToRegisterPlayer()

    fun goToUpdatePlayer(player: PlayerEntity?)

    //fun goToGroup(groupId: Long)

}