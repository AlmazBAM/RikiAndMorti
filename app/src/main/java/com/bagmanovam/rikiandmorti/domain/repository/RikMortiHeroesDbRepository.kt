package com.bagmanovam.rikiandmorti.domain.repository

import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import kotlinx.coroutines.flow.Flow

interface RikMortiHeroesDbRepository {

    suspend fun getAllRikMortiHeroes(): Flow<List<RikMortiHero>>
    suspend fun getRikMortiHero(noteId: Int): RikMortiHero
    suspend fun saveAllRikMortiHeroes(items: List<RikMortiHero>)
}