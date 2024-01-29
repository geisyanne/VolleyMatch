package co.geisyanne.meuapp.presentation.drawTeams.player.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.domain.repository.PlayerRepository

class PlayerListViewModel(
    private val repository: PlayerRepository
) : ViewModel() {

    fun deletePlayer(id: Long) {

    }

    fun deleteSelectedPlayers(ids: List<Long>) {

    }

    /*fun getPlayerByName(name: String): List<PlayerEntity?> {

    }

    fun getAllPlayers(): LiveData<List<PlayerEntity>> {

    }*/






}