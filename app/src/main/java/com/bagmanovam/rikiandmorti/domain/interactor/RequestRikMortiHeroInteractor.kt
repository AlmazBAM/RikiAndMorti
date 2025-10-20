package com.bagmanovam.rikiandmorti.domain.interactor

import com.bagmanovam.nasa_planets.core.domain.Result
import com.bagmanovam.rikiandmorti.core.domain.NetworkError
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero
import com.bagmanovam.rikiandmorti.domain.repository.SearchRikMortiHeroesRepository
import com.bagmanovam.rikiandmorti.domain.useCase.RequestRikMortiHeroUseCase
import com.bagmanovam.rikiandmorti.domain.useCase.RequestRikMortiHeroesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RequestRikMortiHeroInteractor(private val repository: SearchRikMortiHeroesRepository) :
    RequestRikMortiHeroUseCase {
    override suspend fun invoke(itemId: Int): Result<RikMortiHero, NetworkError> {
        return withContext(Dispatchers.IO) {
            repository.requestRikMortiHero(itemId)
        }
    }
}