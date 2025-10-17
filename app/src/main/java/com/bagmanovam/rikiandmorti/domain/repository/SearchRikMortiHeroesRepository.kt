package com.bagmanovam.rikiandmorti.domain.repository

import com.bagmanovam.nasa_planets.core.domain.Result
import com.bagmanovam.rikiandmorti.core.domain.NetworkError
import com.bagmanovam.rikiandmorti.domain.model.RikMortiHero

interface SearchRikMortiHeroesRepository {
    suspend fun requestRikMortiHeroes(count: Int): Result<List<RikMortiHero>, NetworkError>
}