package com.bagmanovam.rikiandmorti.domain.repository

import androidx.room.Query
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import kotlinx.coroutines.flow.Flow

interface RikMortiHeroesDbRepository {

    fun getAllRikMortiHeroes(): Flow<List<RikMortiHero>>
    fun searchRikMortiHeroes(query: String): Flow<List<RikMortiHero>>
    suspend fun getRikMortiHero(noteId: Int): RikMortiHero
    suspend fun saveAllRikMortiHeroes(items: List<RikMortiHero>)
}