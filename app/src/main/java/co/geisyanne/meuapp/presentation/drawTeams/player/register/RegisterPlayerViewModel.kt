package co.geisyanne.meuapp.presentation.drawTeams.player.register

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.geisyanne.meuapp.R

import co.geisyanne.meuapp.domain.model.Group
import co.geisyanne.meuapp.domain.repository.PlayerRepository
import kotlinx.coroutines.launch

class RegisterPlayerViewModel(
    private val repository: PlayerRepository
) : ViewModel() {

    // NOTIFICAR QUANDO UM USER FOR INSERIDO
    private val _playerStateEventData = MutableLiveData<PlayerState>()
    val playerStateEventData: LiveData<PlayerState> get() = _playerStateEventData

    // NOTIFICAR ERRO NO CATCH
    private val _messageEventData = MutableLiveData<Int>()
    val messageEventData: LiveData<Int> get() = _messageEventData


    fun addOrUpdatePlayer(name: String, position: Int?, level: Int?, group: Int?, id: Long = 0) {
        if (id > 0) {
            updatePlayer(id, name, position, level, group)
        } else {
            insertPlayer(name, position, level, group)
        }

    }

     private fun insertPlayer(name: String, position: Int?, level: Int?, group: Int?) =
        viewModelScope.launch {
            try {
                val id = repository.insertPlayer(name, position, level, group)
                if (id > 0) {
                    _playerStateEventData.value = PlayerState.Inserted
                    _messageEventData.value = R.string.player_inserted_successfully
                }

            } catch (e: Exception) {
                _messageEventData.value = R.string.player_error_to_insert
                Log.e(TAG, "Erro ao inserir jogador", e)
            }
        }

    private fun updatePlayer(id: Long, name: String, position: Int?, level: Int?, group: Int?) =
        viewModelScope.launch {
            try {
                repository.updatePlayer(id, name, position, level, group)

                _playerStateEventData.value = PlayerState.Updated
                _messageEventData.value = R.string.player_updated_successfully
            } catch (e: Exception) {
                _messageEventData.value = R.string.player_error_to_updated
                Log.e(TAG, "Erro ao editar jogador", e)
            }
        }



    // PARA REPRESENTAR UM CONJUNTO DE ESTADOS/TIPOS
    sealed class PlayerState {
        data object Inserted : PlayerState()
        data object Updated : PlayerState()
        data object Deleted : PlayerState()
    }

    companion object {
        private val TAG = RegisterPlayerViewModel::class.java.simpleName
    }

}