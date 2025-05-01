package database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ivax.descarregarvideos.dao.PlayListDao
import com.ivax.descarregarvideos.dao.PlaylistSavedVideoCrossRefDao
import com.ivax.descarregarvideos.dao.VideoDao
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.entities.converters.DateTimeConverter
import com.ivax.descarregarvideos.entities.relationships.PlaylistWithSavedVideos

/*
* , autoMigrations = [
    AutoMigration(from = 2,to = 3)
]
* */
@Database(entities = [SavedVideo::class, Playlist::class, PlaylistSavedVideoCrossRef::class],exportSchema = true, version = 12,autoMigrations = [
    AutoMigration(from = 2,to = 3),
    AutoMigration(from = 3,to = 4),
    AutoMigration(from = 4,to = 5),
    AutoMigration(from = 6,to = 7),
    AutoMigration(from = 7,to = 8),
    AutoMigration(from = 8,to = 9),
    AutoMigration(from = 10,to = 11),
    //AutoMigration(from = 11,to = 12),
    //AutoMigration(from = 12,to = 13)
])
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    abstract fun playlistDao(): PlayListDao
    abstract fun playlistSavedVideoCrossRefDao(): PlaylistSavedVideoCrossRefDao
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

