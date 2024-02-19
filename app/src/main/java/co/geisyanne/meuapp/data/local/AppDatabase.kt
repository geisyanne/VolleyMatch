package co.geisyanne.meuapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.geisyanne.meuapp.data.local.dao.GroupDao
import co.geisyanne.meuapp.data.local.dao.PlayerDao
import co.geisyanne.meuapp.data.local.entity.GroupEntity
import co.geisyanne.meuapp.data.local.entity.GroupPlayerCrossRef
import co.geisyanne.meuapp.data.local.entity.PlayerEntity


@Database(
    entities = [PlayerEntity::class, GroupEntity::class, GroupPlayerCrossRef::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val playerDao: PlayerDao
    abstract val groupDao: GroupDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance: AppDatabase? = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        name = "app_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}