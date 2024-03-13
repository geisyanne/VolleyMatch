package co.geisyanne.volleymatch.presentation.drawTeams.draw

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.geisyanne.volleymatch.domain.repository.PlayerRepository

class DrawViewModel(
    private val repository: PlayerRepository
) : ViewModel() {

    // NOTIFICAR QUANDO UM USER FOR DELETADO
    private val _playerStateEventData = MutableLiveData<PlayerState>()
    val playerStateEventData: LiveData<PlayerState> get() = _playerStateEventData

    // NOTIFICAR ERRO NO CATCH
    private val _messageEventData = MutableLiveData<Int>()
    val messageEventData: LiveData<Int> get() = _messageEventData


    val allPlayersEvent = repository.getAllPlayers()


    sealed class PlayerState {

    }

    companion object {
        private val TAG = DrawViewModel::class.java.simpleName
    }

}