package co.geisyanne.meuapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.geisyanne.meuapp.data.dao.PlayerDao
import co.geisyanne.meuapp.data.entity.PlayerEntity


@Database(entities = [PlayerEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val playerDao: PlayerDao

    companion object {
        @Volatile
        private  var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance: AppDatabase? = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        name = "app_database"
                    ).build()
                }
                return instance
            }
        }
    }
}