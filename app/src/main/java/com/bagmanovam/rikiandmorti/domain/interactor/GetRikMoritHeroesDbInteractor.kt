package com.bagmanovam.rikiandmorti.domain.interactor

import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import com.bagmanovam.rikiandmorti.domain.repository.RikMortiHeroesDbRepository
import com.bagmanovam.rikiandmorti.domain.useCase.GetRikMoritHeroesDbUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetRikMoritHeroesDbInteractor(
    private val repository: RikMortiHeroesDbRepository
) : GetRikMoritHeroesDbUseCase {
    override fun invoke(): Flow<List<RikMortiHero>> {
        return  repository.getAllRikMortiHeroes()
    }
}