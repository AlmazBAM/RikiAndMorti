package com.bagmanovam.rikiandmorti.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface RikMoritDao {

    @Insert(RikMortiEntity::class, REPLACE)
    fun addRikMortiHeroes(items: List<RikMortiEntity>)

    @Query("SELECT * FROM rik_morti_items")
    fun getRikMortiHeroes(): Flow<List<RikMortiEntity>>

    @Query("SELECT DISTINCT * FROM rik_morti_items WHERE name LIKE '%' || :query || '%'")
    fun searchRikMortiHeroes(query: String): Flow<List<RikMortiEntity>>

    @Query("SELECT * FROM rik_morti_items WHERE id=:noteId")
    fun getRikMoritHero(noteId: Int): RikMortiEntity
}