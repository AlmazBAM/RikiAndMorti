package com.bagmanovam.rikiandmorti.domain.interactor

import com.bagmanovam.nasa_planets.core.domain.Result
import com.bagmanovam.rikiandmorti.core.domain.NetworkError
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import com.bagmanovam.rikiandmorti.domain.repository.SearchRikMortiHeroesRepository
import com.bagmanovam.rikiandmorti.domain.useCase.RequestRikMortiHeroesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RequestRikMortiHeroesInteractor(private val repository: SearchRikMortiHeroesRepository) :
    RequestRikMortiHeroesUseCase {
    override suspend fun invoke(count: Int): Result<List<RikMortiHero>, NetworkError> {
        return withContext(Dispatchers.IO) {
            repository.requestRikMortiHeroes(count)
        }
    }
}