package co.geisyanne.volleymatch.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import co.geisyanne.volleymatch.data.local.entity.PlayerEntity
import co.geisyanne.volleymatch.data.local.relation.PlayerWithGroups


@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(playerEntity: PlayerEntity): Long

    @Update
    suspend fun updatePlayer(playerEntity: PlayerEntity)

    @Query("DELETE FROM player_table WHERE playerId = :id")
    suspend fun deletePlayer(id: Long)

    @Query("DELETE FROM player_table WHERE playerId IN (:ids)")
    suspend fun deleteSelectedPlayers(ids: List<Long>)

    @Query("SELECT * FROM player_table WHERE name LIKE :name || '%' ORDER BY name ASC")
    fun getPlayerByName(name: String): LiveData<List<PlayerEntity>>

    @Query("SELECT * FROM player_table ORDER BY name ASC")
    fun getAllPlayers(): LiveData<List<PlayerEntity>>

    @Transaction
    @Query("SELECT * FROM player_table WHERE playerId = :playerId")
    fun getGroupsForPlayer(playerId: Long): LiveData<List<PlayerWithGroups>>

}