package database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ivax.descarregarvideos.dao.VideoDao
import com.ivax.descarregarvideos.entities.SavedVideo

/*
* , autoMigrations = [
    AutoMigration(from = 2,to = 3)
]
* */
@Database(entities = [SavedVideo::class],exportSchema = true, version = 4,autoMigrations = [
    AutoMigration(from = 2,to = 3),
    AutoMigration(from = 3,to = 4)
])
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "DescarregarVideosDB"
                )
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}