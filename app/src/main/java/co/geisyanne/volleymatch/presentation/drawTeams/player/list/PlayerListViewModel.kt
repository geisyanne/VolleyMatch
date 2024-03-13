package co.geisyanne.volleymatch.presentation.drawTeams.player.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.data.local.entity.PlayerEntity
import co.geisyanne.volleymatch.domain.repository.PlayerRepository
import kotlinx.coroutines.launch

class PlayerListViewModel(
    private val repository: PlayerRepository
) : ViewModel() {

    // NOTIFICAR QUANDO UM USER FOR DELETADO
    private val _playerStateEventData = MutableLiveData<PlayerState>()
    val playerStateEventData: LiveData<PlayerState> get() = _playerStateEventData

    // NOTIFICAR ERRO NO CATCH
    private val _messageEventData = MutableLiveData<Int>()
    val messageEventData: LiveData<Int> get() = _messageEventData


    val allPlayersEvent = repository.getAllPlayers()


    fun deletePlayer(position: Int) = viewModelScope.launch {

        val player = getPlayerByPosition(position)

        try {
            if (player != null && player.playerId > 0)
                repository.deletePlayer(player.playerId)

            _playerStateEventData.value = PlayerState.Deleted
            _messageEventData.value = R.string.player_deleted_successfully
        } catch (e: Exception) {
            _messageEventData.value = R.string.player_error_to_delete
            Log.e(TAG, e.toString())
        }
    }

    private fun getPlayerByPosition(position: Int): PlayerEntity? {
        val player = allPlayersEvent.value
        return player?.getOrNull(position)
    }

    fun deleteSelectedPlayers(players: List<PlayerEntity>?) = viewModelScope.launch {

        try {
            val ids = players?.map { it.playerId } ?: emptyList()
            repository.deleteSelectedPlayers(ids)

            _playerStateEventData.value = PlayerState.Deleted
            _messageEventData.value = R.string.player_deleted_successfully
        } catch (e: Exception) {
            _messageEventData.value = R.string.player_error_to_delete
            Log.e(TAG, e.toString())
        }
    }

    fun searchPlayer(name: String): LiveData<List<PlayerEntity>> {
        return repository.getPlayerByName(name)
    }

    sealed class PlayerState {
        data object Deleted : PlayerState()
    }

    companion object {
        private val TAG = PlayerListViewModel::class.java.simpleName
    }

}