package co.geisyanne.meuapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import co.geisyanne.meuapp.data.local.entity.GroupEntity
import co.geisyanne.meuapp.data.local.entity.GroupPlayerCrossRef
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import co.geisyanne.meuapp.data.local.relation.GroupWithPlayers
import co.geisyanne.meuapp.data.local.relation.PlayerWithGroups
import kotlinx.coroutines.flow.Flow


@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(groupEntity: GroupEntity): Long

    @Update
    suspend fun updateGroup(groupEntity: GroupEntity)

    @Query("DELETE FROM group_table WHERE groupId = :id")
    suspend fun deleteGroup(id: Long)

    @Query("DELETE FROM group_table WHERE groupId IN (:ids)")
    suspend fun deleteSelectedGroups(ids: List<Long>)

    @Query("SELECT * FROM group_table WHERE name LIKE :name || '%' ORDER BY name ASC")
    fun getGroupByName(name: String): LiveData<List<GroupEntity>>

    @Query("SELECT * FROM group_table ORDER BY name ASC")
    fun getAllGroups(): LiveData<List<GroupEntity>>

    // RELATIONSHIP GROUP-PLAYER

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelation(crossRef: GroupPlayerCrossRef)

    @Delete
    suspend fun deleteRelation(crossRef: GroupPlayerCrossRef)

    @Transaction
    @Query("SELECT * FROM group_table WHERE groupId = :groupId")
    fun getPlayersInGroup(groupId: Long): LiveData<List<GroupWithPlayers>>





}