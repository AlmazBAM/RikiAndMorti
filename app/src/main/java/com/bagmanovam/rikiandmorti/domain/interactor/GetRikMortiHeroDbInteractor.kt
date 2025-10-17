package com.bagmanovam.rikiandmorti.domain.interactor

import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import com.bagmanovam.rikiandmorti.domain.repository.RikMortiHeroesDbRepository
import com.bagmanovam.rikiandmorti.domain.useCase.GetRikMortiHeroDbUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetRikMortiHeroDbInteractor(
    private val repository: RikMortiHeroesDbRepository,
) : GetRikMortiHeroDbUseCase {
    override suspend fun invoke(itemId: Int): RikMortiHero {
        return withContext(Dispatchers.IO) { repository.getRikMortiHero(itemId) }
    }
}