package com.bagmanovam.rikiandmorti.domain.useCase

import com.bagmanovam.nasa_planets.core.domain.Result
import com.bagmanovam.rikiandmorti.core.domain.NetworkError
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero

interface RequestRikMortiHeroesUseCase {
    suspend operator fun invoke(count: Int): Result<List<RikMortiHero>, NetworkError>
}