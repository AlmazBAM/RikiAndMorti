package com.bagmanovam.rikiandmorti.domain.interactor

import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import com.bagmanovam.rikiandmorti.domain.repository.RikMortiHeroesDbRepository
import com.bagmanovam.rikiandmorti.domain.useCase.SearchRikMoritHeroesDbUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SearchRikMoritHeroesDbInteractor(
    private val repository: RikMortiHeroesDbRepository
) : SearchRikMoritHeroesDbUseCase {
    override suspend fun invoke(query: String): Flow<List<RikMortiHero>> {
        return withContext(Dispatchers.IO) { repository.getAllRikMortiHeroes() }
    }
}