package co.geisyanne.volleymatch.presentation.drawTeams.group

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.geisyanne.volleymatch.R
import co.geisyanne.volleymatch.data.local.entity.GroupEntity
import co.geisyanne.volleymatch.data.local.relation.GroupWithPlayers
import co.geisyanne.volleymatch.domain.repository.GroupRepository
import kotlinx.coroutines.launch

class GroupViewModel(
    private val repository: GroupRepository
) : ViewModel() {

    // TODO: USAR EVENT PARA PROGRESS

    // NOTIFICAR QUANDO UM USER FOR DELETADO
    private val _groupStateEventData = MutableLiveData<GroupState>()
    val groupStateEventData: LiveData<GroupState> get() = _groupStateEventData

    // NOTIFICAR ERRO NO CATCH
    private val _messageEventData = MutableLiveData<Int>()
    val messageEventData: LiveData<Int> get() = _messageEventData

    val allGroupsEvent = repository.getAllGroups()

    fun addOrUpdateGroup(name: String, id: Long = 0) {
        if (id > 0) {
            updateGroup(id, name)
        } else {
            insertGroup(name)
        }
    }

    private fun insertGroup(name: String) =
        viewModelScope.launch {
            try {
                val id = repository.insertGroup(name)
                if (id > 0) {
                    _groupStateEventData.value = GroupState.Inserted
                    _messageEventData.value = R.string.group_inserted_successfully
                }

            } catch (e: Exception) {
                _messageEventData.value = R.string.group_error_to_insert
                Log.e(TAG, "Erro ao inserir grupo", e)
            }
        }

    private fun updateGroup(id: Long, name: String) =
        viewModelScope.launch {
            try {
                repository.updateGroup(id, name)

                _groupStateEventData.value = GroupState.Updated
                _messageEventData.value = R.string.group_updated_successfully
            } catch (e: Exception) {
                _messageEventData.value = R.string.group_error_to_updated
                Log.e(TAG, "Erro ao editar grupo", e)
            }
        }

    fun deleteGroup(position: Int) = viewModelScope.launch {

        val group = getGroupByPosition(position)

        try {
            if (group != null && group.groupId > 0)
                repository.deleteGroup(group.groupId)

            _groupStateEventData.value = GroupState.Deleted
            _messageEventData.value = R.string.group_deleted_successfully
        } catch (e: Exception) {
            _messageEventData.value = R.string.group_error_to_delete
            Log.e(TAG, e.toString())
        }
    }

    private fun getGroupByPosition(position: Int): GroupEntity? {
        val group = allGroupsEvent.value
        return group?.getOrNull(position)
    }

    fun deleteSelectedGroups(groups: List<GroupEntity>?) = viewModelScope.launch {

        try {
            val ids = groups?.map { it.groupId } ?: emptyList()
            repository.deleteSelectedGroups(ids)

            _groupStateEventData.value = GroupState.Deleted
            _messageEventData.value = R.string.group_deleted_successfully
        } catch (e: Exception) {
            _messageEventData.value = R.string.group_error_to_delete
            Log.e(TAG, e.toString())
        }
    }

    fun searchGroup(name: String): LiveData<List<GroupEntity>> {
        return repository.getGroupByName(name)
    }

    fun insertRelation(groupId: Long, playerId: Long) {
        //TODO
    }

    fun deleteRelation(groupId: Long, playerId: Long) {
        //TODO
    }

    fun getPlayersInGroup(groupId: Long): LiveData<List<GroupWithPlayers>> {
        return repository.getPlayersInGroup(groupId)
    }

    sealed class GroupState {
        data object Inserted : GroupState()
        data object Updated : GroupState()
        data object Deleted : GroupState()
    }

    companion object {
        private val TAG = GroupViewModel::class.java.simpleName
    }



}