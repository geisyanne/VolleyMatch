package co.geisyanne.meuapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import co.geisyanne.meuapp.data.local.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlayer(playerEntity: PlayerEntity): Long

    @Update
    suspend fun updatePlayer(playerEntity: PlayerEntity)

    @Query("DELETE FROM player WHERE id = :id")
    suspend fun deletePlayer(id: Long)
    @Query("DELETE FROM player WHERE id IN (:ids)")
    suspend fun deleteSelectedPlayers(ids: List<Long>)

    @Query("SELECT * FROM player WHERE name = :name ORDER BY name ASC")
    suspend fun getPlayerByName(name: String): List<PlayerEntity?>

    @Query("SELECT * FROM player ORDER BY name ASC")
    fun getAllPlayers(): Flow<List<PlayerEntity>>



}