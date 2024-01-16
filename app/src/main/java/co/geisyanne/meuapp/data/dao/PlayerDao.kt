package co.geisyanne.meuapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import co.geisyanne.meuapp.data.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlayerDao {

    @Upsert()
    suspend fun upsertPlayer(playerEntity: PlayerEntity): Long

    @Delete()
    suspend fun deletePlayer(playerEntity: PlayerEntity)

    @Query("SELECT * FROM player WHERE name = :name")
    suspend fun getPlayerByName(name: String): PlayerEntity?

    @Query("SELECT * FROM player")
    fun getAllPlayer(): Flow<List<PlayerEntity>>



}