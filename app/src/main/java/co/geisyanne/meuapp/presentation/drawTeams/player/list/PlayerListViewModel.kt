package co.geisyanne.meuapp.presentation.drawTeams.player.list

import android.icu.text.Transliterator.Position
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.geisyanne.meuapp.R
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.domain.repository.PlayerRepository
import co.geisyanne.meuapp.presentation.drawTeams.player.register.RegisterPlayerViewModel
import kotlinx.coroutines.launch

class PlayerListViewModel(
    private val repository: PlayerRepository
) : ViewModel() {

    // NOTIFICAR QUANDO UM USER FOR INSERIDO
    private val _playerStateEventData = MutableLiveData<PlayerState>()
    val playerStateEventData: LiveData<PlayerState> get() = _playerStateEventData

    // NOTIFICAR ERRO NO CATCH
    private val _messageEventData = MutableLiveData<Int>()
    val messageEventData: LiveData<Int> get() = _messageEventData


    val allPlayersEvent = repository.getAllPlayers()


    fun deletePlayer(position: Int) = viewModelScope.launch {

        val player = getPlayerByPosition(position)

        try {
            if (player != null && player.id > 0)
                repository.deletePlayer(player.id)

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


    sealed class PlayerState {
        data object Deleted : PlayerState()
    }

    companion object {
        private val TAG = PlayerListViewModel::class.java.simpleName
    }

}