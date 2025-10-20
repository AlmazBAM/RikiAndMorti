package com.bagmanovam.rikiandmorti.data.repository

import com.bagmanovam.rikiandmorti.data.db.RikMoritDao
import com.bagmanovam.rikiandmorti.data.mapper.entitiesToDomains
import com.bagmanovam.rikiandmorti.data.mapper.entityToDomain
import com.bagmanovam.rikiandmorti.data.mapper.toEntities
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import com.bagmanovam.rikiandmorti.domain.repository.RikMortiHeroesDbRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RikMortiHeroesDbRepositoryImpl(private val dao: RikMoritDao) : RikMortiHeroesDbRepository {
    override fun getAllRikMortiHeroes(): Flow<List<RikMortiHero>> {
        return dao.getRikMortiHeroes().map { it.entitiesToDomains() }
    }

    override fun searchRikMortiHeroes(query: String): Flow<List<RikMortiHero>> {
        return dao.searchRikMortiHeroes(query).map { it.entitiesToDomains() }
    }

    override suspend fun getRikMortiHero(noteId: Int): RikMortiHero {
        return dao.getRikMoritHero(noteId).entityToDomain()
    }

    override suspend fun saveAllRikMortiHeroes(items: List<RikMortiHero>) {
        dao.addRikMortiHeroes(items.toEntities())
    }
}