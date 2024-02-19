package co.geisyanne.meuapp.data.local.repository

import androidx.lifecycle.LiveData
import co.geisyanne.meuapp.data.local.dao.GroupDao
import co.geisyanne.meuapp.data.local.entity.GroupEntity
import co.geisyanne.meuapp.data.local.entity.GroupPlayerCrossRef
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.data.local.relation.GroupWithPlayers
import co.geisyanne.meuapp.domain.repository.GroupRepository

class GroupLocalDataSource(
    private val groupDao: GroupDao
) : GroupRepository {

    override suspend fun insertGroup(name: String): Long {
        val group = GroupEntity(
            name = name,
        )
        return groupDao.insertGroup(group)
    }

    override suspend fun updateGroup(id: Long, name: String) {
        val group = GroupEntity(
            groupId = id,
            name = name,
        )
        groupDao.updateGroup(group)
    }

    override suspend fun deleteGroup(id: Long) {
        groupDao.deleteGroup(id)
    }

    override suspend fun deleteSelectedGroups(ids: List<Long>) {
        groupDao.deleteSelectedGroups(ids)
    }

    override fun getGroupByName(name: String): LiveData<List<GroupEntity>> {
        return groupDao.getGroupByName(name)
    }

    override fun getAllGroups(): LiveData<List<GroupEntity>> {
        return groupDao.getAllGroups()
    }

    override suspend fun insertRelation(groupId: Long, playerId: Long) {
        val crossRef = GroupPlayerCrossRef(
            groupId = groupId,
            playerId = playerId
        )
        groupDao.insertRelation(crossRef)
    }

    override suspend fun deleteRelation(groupId: Long, playerId: Long) {
        val crossRef = GroupPlayerCrossRef(
            groupId = groupId,
            playerId = playerId
        )
        groupDao.deleteRelation(crossRef)
    }

    override fun getPlayersInGroup(groupId: Long): LiveData<List<GroupWithPlayers>> {
        return groupDao.getPlayersInGroup(groupId)
    }


}