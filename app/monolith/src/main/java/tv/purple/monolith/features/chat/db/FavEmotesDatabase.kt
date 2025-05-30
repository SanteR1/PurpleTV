package tv.purple.monolith.features.chat.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tv.purple.monolith.features.chat.db.entities.FavEmoteEntity
import tv.twitch.android.app.core.ApplicationContext

@Database(entities = [FavEmoteEntity::class], version = 1, exportSchema = false)
abstract class FavEmotesDatabase : RoomDatabase() {
    abstract fun favEmotesDAO(): FavEmotesDAO

    companion object {
        private const val DATABASE_NAME = "purpletv_favemotes"

        val INSTANCE by lazy {
            Room.databaseBuilder(
                ApplicationContext.getInstance().getContext(),
                FavEmotesDatabase::class.java, DATABASE_NAME
            ).build()
        }
    }
}