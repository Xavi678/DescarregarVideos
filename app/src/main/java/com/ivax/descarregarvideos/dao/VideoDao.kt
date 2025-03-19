package com.ivax.descarregarvideos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ivax.descarregarvideos.entities.SavedVideo
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    /*@Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User*/

    @Query("SELECT * FROM savedvideo")
    fun getAll(): Flow<List<SavedVideo>>

    @Insert
    fun insertAll(vararg savedVideo: SavedVideo)

    /*@Delete
    fun delete(user: User)*/
}