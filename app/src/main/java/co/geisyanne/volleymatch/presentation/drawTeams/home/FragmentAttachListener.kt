package co.geisyanne.volleymatch.presentation.drawTeams.home

import co.geisyanne.volleymatch.data.local.entity.PlayerEntity
import co.geisyanne.volleymatch.data.local.entity.TeamEntity
import co.geisyanne.volleymatch.domain.model.Team

interface FragmentAttachListener {

    fun goToRegisterPlayer()

    fun goToUpdatePlayer(player: PlayerEntity?)

    fun goToResult(players: List<PlayerEntity>, qtdPlayer: Int, pos: Boolean, lvl: Boolean)

    fun goToEditResult(teams: MutableList<TeamEntity>, pos: Boolean)

    //fun goToGroup(groupId: Long)

}