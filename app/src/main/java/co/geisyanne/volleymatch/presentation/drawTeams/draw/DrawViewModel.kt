package co.geisyanne.volleymatch.presentation.drawTeams.draw

import androidx.lifecycle.ViewModel
import co.geisyanne.volleymatch.domain.repository.PlayerRepository

class DrawViewModel(
    repository: PlayerRepository
) : ViewModel() {

    val allPlayersEvent = repository.getAllPlayers()

}