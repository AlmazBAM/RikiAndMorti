package com.bagmanovam.rikiandmorti.domain.interactor

import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import com.bagmanovam.rikiandmorti.domain.repository.RikMortiHeroesDbRepository
import com.bagmanovam.rikiandmorti.domain.useCase.SaveRikMoritHeroesDbUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveRikMortiHeroesDbInteractor(
    private val repository: RikMortiHeroesDbRepository
) : SaveRikMoritHeroesDbUseCase {
    override suspend fun invoke(items: List<RikMortiHero>) {
        withContext(Dispatchers.IO) { repository.saveAllRikMortiHeroes(items) }
    }
}